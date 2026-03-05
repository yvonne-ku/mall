package com.noinch.mall.biz.order.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.micrometer.common.util.StringUtils;
import org.apache.seata.core.context.RootContext;
import org.springframework.stereotype.Component;

/**
 * OpenFeign 自定义拦截器
 * Spring Cloud OpenFeign 会自动发现并应用它到所有 @FeignClient 的请求上
 * 因为 OpenFeign 的自动配置会从 Spring 容器中收集所有 RequestInterceptor 类型的 Bean
 */
@Component
public class SeataFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String xid = RootContext.getXID();
        if (!StringUtils.isEmpty(xid)) {
            // Seata 默认使用的 Header Key 是 "TX_XID" 或 "XID"
            // 为保证兼容性，可以同时设置多个 Key，或者统一使用 RootContext.KEY_XID
            template.header(RootContext.KEY_XID, xid);
            template.header("TX_XID", xid);
            template.header("XID", xid);
        }
    }
}
