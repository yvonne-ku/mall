
package com.noinch.mall.springboot.starter.cache;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.noinch.mall.springboot.starter.base.Singleton;
import com.noinch.mall.springboot.starter.cache.config.properties.RedisDistributedProperties;
import com.noinch.mall.springboot.starter.cache.core.CacheLoadMissingHandler;
import com.noinch.mall.springboot.starter.cache.core.CacheLoader;
import com.noinch.mall.springboot.starter.cache.toolkit.CacheUtil;
import com.noinch.mall.springboot.starter.cache.toolkit.FastJson2Util;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;


import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 分布式缓存之操作 Redis 模版代理
 * 底层通过 {@link RedissonClient}、{@link StringRedisTemplate} 完成外观接口行为
 *
 */
@RequiredArgsConstructor
public class StringRedisTemplateProxy implements DistributedCache {
    
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisDistributedProperties redisProperties;
    private final RedissonClient redissonClient;
    
    private static final String LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH = "lua/putIfAllAbsent.lua";
    private static final String SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX = "safe_get_distributed_lock_get:";

    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public Long delete(Collection<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    /**
     * 如果目标类型是String，直接返回；否则使用FastJson2反序列化为指定类型。
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        // 查目标类型 clazz 是否是 String 类型或其子类。如果是，直接返回原始字符串值，避免不必要的JSON解析。
        if (String.class.isAssignableFrom(clazz)) {
            return (T) value;
        }
        // 如果目标类型不是 String，使用 FastJson2 将 JSON 字符串反序列化为目标类型的对象。
        return JSON.parseObject(value, FastJson2Util.buildType(clazz));
    }

    @Override
    public <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {
        return get(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit());
    }
    
    @Override
    public <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        T result = get(key, clazz);

        // 缓存结果不等于空或空字符串直接返回
        if (!CacheUtil.isNullOrBlank(result)) {
            return result;
        }
        // 缓存不存在，调用 CacheLoader 加载数据并设置缓存
        return loadAndSet(key, cacheLoader, timeout, redisProperties.getValueTimeUnit(), false, null);
    }
    
    @Override
    public <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter) {
        return safeGet(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit(),bloomFilter, null);
    }
    
    @Override
    public <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        return safeGet(key, clazz, cacheLoader, timeout, timeUnit, bloomFilter, null);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheLoadMissingHandler<String> cacheLoadMissingHandler) {
        return safeGet(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit(), bloomFilter, cacheLoadMissingHandler);
    }
    
    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheLoadMissingHandler<String> cacheLoadMissingHandler) {

        // 首先检查布隆过滤器，如果布隆过滤器判定 key 不存在，直接返回 null，避免缓存穿透
        if (Optional.ofNullable(bloomFilter).map(each -> !each.contains(key)).orElse(false)) {
            return null;
        }

        // 其次检查缓存中是否存在数据
        T result = get(key, clazz);
        if (!CacheUtil.isNullOrBlank(result)) {
            return result;
        }

        // 最后查询上游数据库，并进行缓存更新构建
        // 使用分布式锁，防止多线程反复请求数据库，构建同一个 key
        RLock lock = redissonClient.getLock(SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX + key);
        lock.lock();
        try {
            // 二次确认，缓存中确实不存在，才更新
            if (CacheUtil.isNullOrBlank(result = get(key, clazz))) {
                // loadAndSet：调用 CacheLoader 加载数据。对于存在的数据，更新到缓存；对于不存在的数据，返回 null，调用 CacheLoadMissingHandler 后置处理。
                if (CacheUtil.isNullOrBlank(result = loadAndSet(key, cacheLoader, timeout, timeUnit, true, bloomFilter))) {
                    Optional.ofNullable(cacheLoadMissingHandler).ifPresent(each -> each.execute(key));
                }
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, redisProperties.getValueTimeout());
    }

    @Override
    public void put(String key, Object value, long timeout) {
        put(key, value, timeout, redisProperties.getValueTimeUnit());
    }
    
    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        String actual = value instanceof String ? (String) value : JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key, actual, timeout, timeUnit);
    }
    
    @Override
    public void safePut(String key, Object value, long timeout, RBloomFilter<String> bloomFilter) {
        safePut(key, value, timeout, redisProperties.getValueTimeUnit(), bloomFilter);
    }
    
    @Override
    public void safePut(String key, Object value, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        put(key, value, timeout, timeUnit);
        bloomFilter.add(key);
    }

    @Override
    public Boolean putIfAllAbsent(@NotNull Collection<String> keys) {
        DefaultRedisScript<Boolean> actual = Singleton.get(LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH, () -> {
            DefaultRedisScript redisScript = new DefaultRedisScript();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH)));
            redisScript.setResultType(Boolean.class);
            return redisScript;
        });
        Boolean result = stringRedisTemplate.execute(actual, Lists.newArrayList(keys), redisProperties.getValueTimeout().toString());
        return result == null ? false : result;
    }


    @Override
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }
    
    @Override
    public Object getInstance() {
        return stringRedisTemplate;
    }
    
    @Override
    public Long countExistingKeys(String... keys) {
        return stringRedisTemplate.countExistingKeys(Lists.newArrayList(keys));
    }
    
    private <T> T loadAndSet(String key, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, boolean safeFlag, RBloomFilter<String> bloomFilter) {
        T result = cacheLoader.load();
        if (CacheUtil.isNullOrBlank(result)) {
            return result;
        }
        if (safeFlag) {
            safePut(key, result, timeout, timeUnit, bloomFilter);
        } else {
            put(key, result, timeout, timeUnit);
        }
        return result;
    }
}
