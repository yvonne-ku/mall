package com.noinch.mall.springboot.starter.cache.core;

/**
 * 从上游 DB 等加载数据
 */
@FunctionalInterface
public interface CacheLoader<T> {

    T load();
}
