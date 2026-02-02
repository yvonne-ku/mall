

package com.noinch.mall.biz.order.application.filter;

import com.noinch.mall.biz.order.application.filter.base.OrderCreateChainFilter;
import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.biz.order.domain.common.OrderCreateErrorCodeEnum;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 订单创建参数正确性检验
 *
 */
@Component
public final class OrderCreateParamVerificationChainHandler implements OrderCreateChainFilter<OrderCreateCommand> {
    
    @Override
    public void handler(OrderCreateCommand requestParam) {
        if (requestParam.getTotalAmount().compareTo(new BigDecimal(0)) == -1 || requestParam.getTotalAmount().compareTo(new BigDecimal(0)) == 0) {
            throw new ClientException(OrderCreateErrorCodeEnum.TOTAL_AMOUNT_ERROR);
        } else if (requestParam.getPayAmount().compareTo(new BigDecimal(0)) == -1 || requestParam.getPayAmount().compareTo(new BigDecimal(0)) == 0) {
            throw new ClientException(OrderCreateErrorCodeEnum.PAY_AMOUNT_ERROR);
        } else if (requestParam.getFreightAmount().compareTo(new BigDecimal(0)) == -1) {
            throw new ClientException(OrderCreateErrorCodeEnum.FREIGHT_AMOUNT_ERROR);
        } else if (requestParam.getTotalAmount().compareTo(requestParam.getPayAmount()) == -1) {
            throw new ClientException(OrderCreateErrorCodeEnum.AMOUNT_VERIFICATION_ERROR);
        }
        // xxx 还有更多订单参数信息需要验证，因为重复工作量所以暂时验证上述这些
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
}
