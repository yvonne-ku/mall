
package com.noinch.mall.springboot.starter.idempotent.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import com.noinch.mall.springboot.starter.idempotent.annotation.Idempotent;

import java.lang.reflect.Method;

/**
 * 幂等注解 AOP 拦截器
 *
 */
@Aspect
public final class IdempotentAspect {
    
    /**
     * 增强方法标记 {@link Idempotent} 注解逻辑
     */
    @Around("@annotation(com.noinch.mall.springboot.starter.idempotent.annotation.Idempotent)")
    public Object idempotentHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        // 从方法上反射获取 Idempotent 注解
        Idempotent idempotent = getIdempotent(joinPoint);
        // 根据 Idempotent 参数，利用工厂模式创建对应的 IdempotentExecuteHandler 实例
        IdempotentExecuteHandler instance = IdempotentExecuteHandlerFactory.getInstance(idempotent.scene(), idempotent.type());
        // 实例执行 execute 方法，执行完成后再执行原方法，最终实例执行 postProcessing 方法
        try {
            instance.execute(joinPoint, idempotent);
            return joinPoint.proceed();
        } catch (RepeatConsumptionException ex) {
            if (!ex.getError()) {
                return null;
            }
            throw ex;
        } finally {
            instance.postProcessing();
            IdempotentContext.clean();
        }
    }
    
    public static Idempotent getIdempotent(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(Idempotent.class);
    }
}
