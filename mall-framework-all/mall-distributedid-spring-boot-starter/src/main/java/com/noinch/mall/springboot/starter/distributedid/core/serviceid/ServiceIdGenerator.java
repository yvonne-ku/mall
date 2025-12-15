package com.noinch.mall.springboot.starter.distributedid.core.serviceid;

import com.noinch.mall.springboot.starter.distributedid.core.IdGenerator;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.SnowflakeIdInfo;

/**
 * 业务 ID 生成器
 *
 */
public interface ServiceIdGenerator extends IdGenerator {

    /**
     * 根据 {@param serviceId} 生成雪花算法 ID
     */
    long nextId(long serviceId);

    /**
     * 根据 {@param serviceId} 生成字符串类型雪花算法 ID
     */
    String nextIdStr(long serviceId);

    /**
     * 解析雪花算法 ID
     */
    SnowflakeIdInfo parseSnowflakeId(long snowflakeId);
}