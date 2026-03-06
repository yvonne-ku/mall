

package com.noinch.mall.springboot.starter.idempotent.core;

import com.noinch.mall.springboot.starter.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 幂等执行处理器
 *
 */
public interface IdempotentExecuteHandler {
    
    /**
     * 幂等处理逻辑
     * wrapper 的意义是，将 handler 需要的参数包装起来，统一管理
     * 这样入参也不会写得很长
     * @param wrapper 幂等参数包装器
     */
    void handler(IdempotentParamWrapper wrapper);
    
    /**
     * 执行幂等处理逻辑
     *
     * @param joinPoint  AOP 方法处理
     * @param idempotent 幂等注解
     */
    void execute(ProceedingJoinPoint joinPoint, Idempotent idempotent);
    
    /**
     * 异常流程处理
     */
    default void exceptionProcessing() {
        
    }
    
    /**
     * 后置处理
     */
    default void postProcessing() {
        
    }
}
