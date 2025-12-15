
package com.noinch.mall.springboot.starter.mybatisplus;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;

/**
 * 自定义雪花算法生成器
 *
 */
public class CustomIdGenerator implements IdentifierGenerator {
    
    @Override
    public Number nextId(Object entity) {
        return SnowflakeIdUtil.nextId();
    }
}
