
package com.noinch.mall.biz.pay.application.convert;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.pay.application.req.PayCommand;
import com.noinch.mall.biz.pay.domain.base.AliPayRequest;
import com.noinch.mall.biz.pay.domain.base.PayRequest;
import com.noinch.mall.biz.pay.domain.base.WeiXinPayRequest;
import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;

import java.util.Objects;

/**
 * 支付请求入参转换器
 *
 */
public final class PayRequestConvert {
    
    /**
     * {@link PayCommand} to {@link PayRequest}
     *
     * @param payCommand 支付请求参数
     * @return {@link PayRequest}
     */
    public static PayRequest command2PayRequest(PayCommand payCommand) {
        PayRequest payRequest = null;
        if (Objects.equals(payCommand.getChannel(), PayChannelEnum.ALI_PAY.getChannel())) {
            payRequest = new AliPayRequest();
            BeanUtil.copyProperties(payCommand, payRequest);
        }
        if (Objects.equals(payCommand.getChannel(), PayChannelEnum.WX_PAY.getChannel())) {
            payRequest = new WeiXinPayRequest();
            BeanUtil.copyProperties(payCommand, payRequest);
        }
        return payRequest;
    }
}
