
package com.noinch.mall.springboot.starter.idempotent.core;

import org.aspectj.lang.ProceedingJoinPoint;
import com.noinch.mall.springboot.starter.idempotent.annotation.Idempotent;

/**
 * 抽象幂等执行处理器
 *
 */
public abstract class AbstractIdempotentTemplate implements IdempotentExecuteHandler {
    
    /**
     * 构建幂等验证过程中所需要的参数包装器
     *
     * @param joinPoint AOP 方法处理
     * @return 幂等参数包装器
     */
    protected abstract IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint);
    
    /**
     * 执行幂等处理逻辑
     * 所有实现类共享的逻辑：先构造参数包装器，再通过 handler 执行幂等逻辑
     *
     * @param joinPoint  AOP 方法处理
     * @param idempotent 幂等注解
     */
    public void execute(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        IdempotentParamWrapper idempotentParamWrapper = buildWrapper(joinPoint).setIdempotent(idempotent);
        handler(idempotentParamWrapper);
    }
}
