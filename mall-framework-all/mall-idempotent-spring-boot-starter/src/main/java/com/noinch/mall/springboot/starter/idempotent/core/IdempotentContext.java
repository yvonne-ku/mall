
package com.noinch.mall.springboot.starter.idempotent.core;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 幂等上下文
 *
 */
public final class IdempotentContext {

    /**
     * 为什么是 static：
     * 在 Web 应用中，一次请求通常由 Tomcat 的一个线程从头到尾处理。我们希望在处理一个请求的各个阶段（拦截器、AOP 切面、Service 层）都能方便地存取当前请求的幂等数据，但又不想把这些数据作为参数层层传递。
     * 设置为 static，所有的线程访问同一个 ThreadLocal 对象，但是 ThreadLocal 内部维护了一个线程到值的映射表，所以不同的线程访问到各自的 Map
     * 很方便在任意阶段可以用 Idempotent.get() 访问
     */
    private static ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    /**
     * 获得上下文 Map
     */
    public static Map<String, Object> get() {
        return CONTEXT.get();
    }

    /**
     * 从上下文获得 key 的值
     */
    public static Object getKey(String key) {
        Map<String, Object> context = get();
        if (CollUtil.isNotEmpty(context)) {
            return context.get(key);
        }
        return null;
    }

    /**
     * 从上下文获得 key 的（String）值
     */
    public static String getString(String key) {
        Object actual = getKey(key);
        if (actual != null) {
            return actual.toString();
        }
        return null;
    }

    /**
     * 将 key, val 放入上下文
     */
    public static void put(String key, Object val) {
        Map<String, Object> context = get();
        if (CollUtil.isEmpty(context)) {
            context = Maps.newHashMap();
        }
        context.put(key, val);
        putContext(context);
    }

    /**
     * 将多个 key, val 放入上下文
     */
    public static void putContext(Map<String, Object> context) {
        Map<String, Object> threadContext = CONTEXT.get();
        if (CollUtil.isNotEmpty(threadContext)) {
            threadContext.putAll(context);
            return;
        }
        CONTEXT.set(context);
    }

    /**
     * 清理上下文
     */
    public static void clean() {
        CONTEXT.remove();
    }
}
