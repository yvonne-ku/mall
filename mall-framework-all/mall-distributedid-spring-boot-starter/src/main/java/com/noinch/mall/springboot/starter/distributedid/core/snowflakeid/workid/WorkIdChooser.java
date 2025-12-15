package com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid;

/**
 * WorkId选择器接口 - 单一职责：选择WorkId
 */
public interface WorkIdChooser {

    /**
     * 选择WorkId和DataCenterId
     */
    WorkIdWrapper chooseWorkId();

    /**
     * 获取选择器名称（用于日志和监控）
     */
    String getName();
}
