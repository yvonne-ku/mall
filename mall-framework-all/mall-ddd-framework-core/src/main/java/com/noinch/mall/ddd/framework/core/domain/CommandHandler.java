
package com.noinch.mall.ddd.framework.core.domain;

/**
 * 命令处理器
 * */
public interface CommandHandler<T, R> {
    
    /**
     * 命令执行
     *
     */
    R handler(T requestParam);
}
