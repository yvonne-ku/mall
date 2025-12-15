package com.noinch.mall.springboot.starter.distributedid;

import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.Snowflake;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.SnowflakeIdInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultServiceIdGeneratorTest {

    @Test
    public void nextId() {
        Snowflake snowflake = new Snowflake(0, 0);
        SnowflakeIdUtil.initSnowflake(snowflake);
        for (int i = 0; i < 100; i++) {
            long nextId = SnowflakeIdUtil.nextId();
            long nextIdByService = SnowflakeIdUtil.nextIdByService(String.valueOf(i));
            System.out.println(String.format("雪花算法ID: %d, 结合业务ID: %d", nextId, nextIdByService));
        }
    }

    @Test
    public void parseSnowflakeId() {
        Snowflake snowflake = new Snowflake(0, 0);
        SnowflakeIdUtil.initSnowflake(snowflake);
        long nextId = SnowflakeIdUtil.nextId();
        SnowflakeIdInfo snowflakeIdInfo = SnowflakeIdUtil.parseSnowflakeId(nextId);
        System.out.println(nextId);
        System.out.println(snowflakeIdInfo);

        Assertions.assertEquals(0, snowflakeIdInfo.getWorkerId());
        Assertions.assertEquals(0, snowflakeIdInfo.getDataCenterId());
        long currentTime = System.currentTimeMillis();
        Assertions.assertTrue(snowflakeIdInfo.getTimestamp() <= currentTime);
        Assertions.assertTrue(snowflakeIdInfo.getTimestamp() > currentTime - 60000); // 1分钟内
    }

    @Test
    public void parseSnowflakeServiceId() {
        Snowflake snowflake = new Snowflake(0, 0);
        SnowflakeIdUtil.initSnowflake(snowflake);

        long i = 1634852818433081360L;
        long nextIdByService = SnowflakeIdUtil.nextIdByService(String.valueOf(i));
        SnowflakeIdInfo snowflakeIdInfo = SnowflakeIdUtil.parseSnowflakeServiceId(String.valueOf(nextIdByService));
        System.out.println(nextIdByService);
        System.out.println(snowflakeIdInfo);

        // 这个模块没写怎么从 gene 反映射回到 service id，所以没法检验
        long currentTime = System.currentTimeMillis();
        Assertions.assertTrue(snowflakeIdInfo.getTimestamp() <= currentTime);
        Assertions.assertTrue(snowflakeIdInfo.getTimestamp() > currentTime - 60000); // 1分钟内
    }

}
