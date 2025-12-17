
package com.noinch.mall.springboot.starter.designpattern.builder;

import java.io.Serializable;

/**
 * Builder 模式抽象接口
 * 构建者模式：每个配置方法都返回 this，支持链式调用 */
public interface Builder<T> extends Serializable {
    
    /**
     * 构建方法
     *
     * @return 构建后的对象
     */
    T build();
}
