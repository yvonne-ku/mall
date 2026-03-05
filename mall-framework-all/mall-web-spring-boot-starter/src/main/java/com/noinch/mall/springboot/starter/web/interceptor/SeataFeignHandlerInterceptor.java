package com.noinch.mall.springboot.starter.web.interceptor;

import io.seata.core.context.RootContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Spring MVC 拦截器
 * 用于 Seata 全局事务 XID 传递
 */
@Component
public class SeataFeignHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取 XID（尝试多个常用 Key）
        String xid = request.getHeader(RootContext.KEY_XID);      // "tx-xid"
        if (StringUtils.isEmpty(xid)) {
            xid = request.getHeader("TX_XID");
        }
        if (StringUtils.isEmpty(xid)) {
            xid = request.getHeader("XID");
        }
        if (!StringUtils.isEmpty(xid)) {
            RootContext.bind(xid);
        }
        return true;
    }

    /**
     * 整个请求处理完成之后被调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String xid = RootContext.getXID();
        if (!StringUtils.isEmpty(xid)) {
            RootContext.unbind();
        }
    }
}