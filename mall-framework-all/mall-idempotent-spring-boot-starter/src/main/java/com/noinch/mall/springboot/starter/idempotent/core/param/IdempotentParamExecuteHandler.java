
package com.noinch.mall.springboot.starter.idempotent.core.param;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import com.noinch.mall.springboot.starter.idempotent.core.AbstractIdempotentTemplate;
import com.noinch.mall.springboot.starter.idempotent.core.IdempotentContext;
import com.noinch.mall.springboot.starter.idempotent.core.IdempotentParamWrapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于方法参数验证请求幂等性
 *
 */
@RequiredArgsConstructor
public final class IdempotentParamExecuteHandler extends AbstractIdempotentTemplate implements IdempotentParamService {
    
    private final RedissonClient redissonClient;
    
    private final static String LOCK = "lock:param:restAPI";

    /**
     * param 方式构造 wrapper，需要 lockKey
     */
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        String lockKey = String.format("idempotent:path:%s:currentUserId:%s:md5:%s", getServletPath(), getCurrentUserId(), calcArgsMD5(joinPoint));
        return IdempotentParamWrapper.builder().lockKey(lockKey).build();
    }
    
    /**
     * @return 获取当前线程上下文 ServletPath
     */
    private String getServletPath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sra.getRequest().getServletPath();
    }
    
    /**
     * @return 当前操作用户 ID
     */
    private String getCurrentUserId() {
        return null;
    }
    
    /**
     * 对方法的所有参数进行 JSON 序列化后计算 MD5 值，保证参数完全相同时 MD5 才相同。
     * @return joinPoint md5
     */
    private String calcArgsMD5(ProceedingJoinPoint joinPoint) {
        return DigestUtil.md5Hex(JSON.toJSONBytes(joinPoint.getArgs()));
    }
    
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        // 成功加锁，说明当前请求是第一次进来
        // 枷锁失败，说明重复请求
        String lockKey = wrapper.getLockKey();
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            throw new ClientException(wrapper.getIdempotent().message());
        }
        // 放入上下文，后续解锁时需要
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
