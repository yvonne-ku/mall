package com.noinch.mall.biz.pay.domain.base;

import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;
import lombok.Data;

@Data
public final class WeiXinPayCallbackRequest extends AbstractPayCallbackRequest {

    @Override
    public WeiXinPayCallbackRequest getWeiXinPayCallbackRequest() {
        return this;
    }

    @Override
    public String buildMark() {
        return PayChannelEnum.WX_PAY.name();
    }
}
