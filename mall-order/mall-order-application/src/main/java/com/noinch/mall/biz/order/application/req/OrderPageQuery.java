
package com.noinch.mall.biz.order.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.noinch.mall.springboot.starter.convention.page.PageRequest;

/**
 * 订单分页查询
 *
 */
@Data
public class OrderPageQuery extends PageRequest {
    
    /**
     * 用户 ID
     */
    private String userId;
}
