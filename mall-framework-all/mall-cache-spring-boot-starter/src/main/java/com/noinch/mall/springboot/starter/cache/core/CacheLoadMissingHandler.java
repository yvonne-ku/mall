package com.noinch.mall.springboot.starter.cache.core;

/**
 * 当 CacheLoader 加载数据为空时，执行该逻辑。可用于记录日志或把 null key 放入缓存防止缓存穿透。
 */
@FunctionalInterface
public interface CacheLoadMissingHandler<T> {

    void execute(T param);
}
