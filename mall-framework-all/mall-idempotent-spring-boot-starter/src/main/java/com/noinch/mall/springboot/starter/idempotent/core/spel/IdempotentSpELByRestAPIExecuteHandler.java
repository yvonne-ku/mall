

package com.noinch.mall.springboot.starter.idempotent.core.spel;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import com.noinch.mall.springboot.starter.idempotent.annotation.Idempotent;
import com.noinch.mall.springboot.starter.idempotent.core.AbstractIdempotentTemplate;
import com.noinch.mall.springboot.starter.idempotent.core.IdempotentAspect;
import com.noinch.mall.springboot.starter.idempotent.core.IdempotentContext;
import com.noinch.mall.springboot.starter.idempotent.core.IdempotentParamWrapper;
import com.noinch.mall.springboot.starter.idempotent.toolkit.SpELUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 基于 SpEL 方法验证请求幂等性，适用于 RestAPI 场景
 *
 */
@RequiredArgsConstructor
public final class IdempotentSpELByRestAPIExecuteHandler extends AbstractIdempotentTemplate implements IdempotentSpELService {
    
    private final RedissonClient redissonClient;
    
    private final static String LOCK = "lock:spEL:restAPI";

    /**
     * SPEL 构造 wrapper，需要 lockKey
     */
    @SneakyThrows
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        Idempotent idempotent = IdempotentAspect.getIdempotent(joinPoint);
        String key = (String) SpELUtil.parseKey(idempotent.key(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        return IdempotentParamWrapper.builder().lockKey(key).build();
    }
    
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        String lockKey = wrapper.getLockKey();
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            return;
        }
        IdempotentContext.put(LOCK, lock);
    }
    
    @Override
    public void postProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }
}
