
package com.noinch.mall.biz.pay.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.pay.domain.aggregate.Pay;
import com.noinch.mall.biz.pay.domain.common.TradeStatusEnum;
import com.noinch.mall.biz.pay.domain.event.PayResultNotifyMessageEvent;
import com.noinch.mall.biz.pay.domain.repository.PayRepository;
import com.noinch.mall.biz.pay.infrastructure.dao.entity.PayDO;
import com.noinch.mall.biz.pay.infrastructure.dao.mapper.PayMapper;
import com.noinch.mall.biz.pay.infrastructure.mq.producer.PayResultNotifyProducer;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 支付聚合根
 *
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRepositoryImpl implements PayRepository {
    
    private final PayMapper payMapper;
    
    private final PayResultNotifyProducer payResultNotifyProducer;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createPay(Pay pay) {
        PayDO insertPay = new PayDO();
        BeanUtil.copyProperties(pay, insertPay);
        insertPay.setStatus(TradeStatusEnum.WAIT_BUYER_PAY.name());
        int result = payMapper.insert(insertPay);
        if (result <= 0) {
            log.error("支付单创建失败，支付聚合根：{}", JSON.toJSONString(pay));
            throw new ServiceException("支付单创建失败");
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void callbackPay(Pay pay) {
        LambdaQueryWrapper<PayDO> queryWrapper = Wrappers.lambdaQuery(PayDO.class)
                .eq(PayDO::getOrderRequestId, pay.getOrderRequestId());
        PayDO payDO = payMapper.selectOne(queryWrapper);
        if (Objects.isNull(payDO)) {
            log.error("支付单不存在，orderRequestId：{}", pay.getOrderRequestId());
            throw new ServiceException("支付单不存在");
        }
        payDO.setTradeNo(pay.getTradeNo());
        payDO.setStatus(pay.getStatus());
        payDO.setPayAmount(pay.getPayAmount());
        payDO.setGmtPayment(pay.getGmtPayment());
        int result = payMapper.updateById(payDO);
        if (result <= 0) {
            log.error("修改支付单支付结果失败，支付单信息：{}", JSON.toJSONString(payDO));
            throw new ServiceException("修改支付单支付结果失败");
        }

        // 交易成功，回调订单服务告知支付结果，修改订单流转状态
        if (Objects.equals(pay.getStatus(), TradeStatusEnum.TRADE_SUCCESS.name())) {
            PayResultNotifyMessageEvent event = new PayResultNotifyMessageEvent();
            BeanUtil.copyProperties(pay, event);
            payResultNotifyProducer.payResultNotifyMessageSend(event);
        }
    }
}
