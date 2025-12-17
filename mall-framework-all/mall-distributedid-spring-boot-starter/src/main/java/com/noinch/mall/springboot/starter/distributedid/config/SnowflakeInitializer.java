package com.noinch.mall.springboot.starter.distributedid.config;

import cn.hutool.core.date.SystemClock;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.Snowflake;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid.WorkIdChooser;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid.WorkIdWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 雪花算法模板生成
 * */
@Slf4j
public class SnowflakeInitializer {

    /**
     * 工作机器 ID 选择器
     */
    private final WorkIdChooser workIdChooser;

    /**
     * 是否使用 {@link SystemClock} 获取当前时间戳
     */
    @Value("${mall.distributed.id.snowflake.is-use-system-clock:false}")
    private boolean isUseSystemClock;

    public SnowflakeInitializer(WorkIdChooser workIdChooser) {
        this.workIdChooser = workIdChooser;
    }

    /**
     * 初始化雪花算法静态工具类
     */
    public void initializeSnowflake() {
        try {
            WorkIdWrapper workIdWrapper = workIdChooser.chooseWorkId();
            long workId = workIdWrapper.getWorkId();
            long dataCenterId = workIdWrapper.getDataCenterId();

            Snowflake snowflake = new Snowflake(workId, dataCenterId, isUseSystemClock);
            SnowflakeIdUtil.initSnowflake(snowflake);

            log.info("Snowflake initialized successfully using {}: workId={}, dataCenterId={}",
                    workIdChooser.getName(), workIdWrapper.getWorkId(), workIdWrapper.getDataCenterId());
        } catch (Exception e) {
            log.error("Failed to initialize Snowflake", e);
            throw new RuntimeException("Snowflake initialization failed", e);
        }
    }
}
