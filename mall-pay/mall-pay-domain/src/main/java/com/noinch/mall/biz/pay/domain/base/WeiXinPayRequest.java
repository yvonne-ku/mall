package com.noinch.mall.biz.pay.domain.base;

import cn.hutool.core.util.StrUtil;
import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public final class WeiXinPayRequest extends AbstractPayRequest {

    @Override
    public WeiXinPayRequest getWeiXinPayRequest() {
        return this;
    }

    @Override
    public String buildMark() {
        String mark = PayChannelEnum.WX_PAY.name();
        if (StrUtil.isNotBlank(getTradeType())) {
            mark = PayChannelEnum.WX_PAY.name() + "_" + getTradeType();
        }
        return mark;
    }
}

