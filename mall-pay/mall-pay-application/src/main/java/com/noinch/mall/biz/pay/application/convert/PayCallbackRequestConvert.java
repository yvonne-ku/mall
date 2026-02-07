
package com.noinch.mall.biz.pay.application.convert;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.pay.application.req.PayCallbackCommand;
import com.noinch.mall.biz.pay.domain.base.AliPayCallbackRequest;
import com.noinch.mall.biz.pay.domain.base.PayCallbackRequest;
import com.noinch.mall.biz.pay.domain.base.WeiXinPayCallbackRequest;
import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;

import java.util.Objects;

/**
 * 支付回调请求入参转换器
 *
 */
public final class PayCallbackRequestConvert {
    
    /**
     * {@link PayCallbackCommand} to {@link PayCallbackRequest}
     *
     * @param payCallbackCommand 支付回调请求参数
     * @return {@link PayCallbackRequest}
     */
    public static PayCallbackRequest command2PayCallbackRequest(PayCallbackCommand payCallbackCommand) {
        PayCallbackRequest payCallbackRequest = null;
        if (Objects.equals(payCallbackCommand.getChannel(), PayChannelEnum.ALI_PAY.getChannel())) {
            AliPayCallbackRequest aliPayCallbackRequest = new AliPayCallbackRequest();
            BeanUtil.copyProperties(payCallbackCommand, aliPayCallbackRequest);
            aliPayCallbackRequest.setOrderRequestId(payCallbackCommand.getOrderRequestId());
            payCallbackRequest = aliPayCallbackRequest;
        }
        if (Objects.equals(payCallbackCommand.getChannel(), PayChannelEnum.WX_PAY.getChannel())) {
            WeiXinPayCallbackRequest weiXinPayCallbackRequest = new WeiXinPayCallbackRequest();
            BeanUtil.copyProperties(payCallbackCommand, weiXinPayCallbackRequest);
            weiXinPayCallbackRequest.setOrderRequestId(payCallbackCommand.getOrderRequestId());
            payCallbackRequest = weiXinPayCallbackRequest;
        }
        return payCallbackRequest;
    }
}
