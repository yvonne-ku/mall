package com.noinch.mall.springboot.starter.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * 自定义 Redis Key 序列化器
 */
@RequiredArgsConstructor
public class RedisKeySerializer implements InitializingBean, RedisSerializer<String> {

    /**
     * 在 redis 中，为了区分项目和模块，通常会为 key 添加前缀
     * 这对应用程序是透明的，但有助于运维人员识别和管理不同项目和模块的缓存数据
     */
    private final String keyPrefix;

    private final String charsetName;

    /**
     * 使用字符集进行序列化
     */
    private Charset charset;

    @Override
    public byte[] serialize(String key) throws SerializationException {
        String builderKey = keyPrefix + key;
        return builderKey.getBytes(charset);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String keyWithPrefix = new String(bytes, charset);
        return keyWithPrefix.substring(keyPrefix.length());
    }

    /**
     * 从 InitializingBean 继承来的方法
     * 在初始化 Bean 后进行的后置操作
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        charset = Charset.forName(charsetName);
    }
}
