

package com.noinch.mall.springboot.starter.distributedid.core.manager;

import com.noinch.mall.springboot.starter.distributedid.core.serviceid.DefaultServiceIdGenerator;
import com.noinch.mall.springboot.starter.distributedid.core.IdGenerator;
import com.noinch.mall.springboot.starter.distributedid.core.serviceid.ServiceIdGenerator;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IdGeneratorManager是一个单例管理器，负责使用 Map 统一管理项目中所有的 ID 生成器实例。 */
public final class IdGeneratorManager {
    
    /**
     * ID 生成器管理容器
     */
    private static Map<String, IdGenerator> MANAGER = new ConcurrentHashMap<>();

    /**
     * 注册默认 ID 生成器
     */
    static {
        MANAGER.put("default", new DefaultServiceIdGenerator());
    }

    /**
     * 注册 ID 生成器
     */
    public static void registerIdGenerator(@NonNull String resource, @NonNull IdGenerator idGenerator) {
        IdGenerator actual = MANAGER.get(resource);
        if (actual != null) {
            return;
        }
        MANAGER.put(resource, idGenerator);
    }
    
    /**
     * 根据 {@param resource} 获取 ID 生成器
     */
    public static IdGenerator getIdGenerator(@NonNull String resource) {
        return MANAGER.get(resource);
    }
    
    /**
     * 获取默认 ID 生成器 {@link DefaultServiceIdGenerator}
     */
    public static ServiceIdGenerator getDefaultServiceIdGenerator() {
        return Optional.ofNullable(MANAGER.get("default")).map(each -> (ServiceIdGenerator) each).orElse(null);
    }
}
