
package com.noinch.mall.springboot.starter.log.config;

import com.noinch.mall.springboot.starter.log.annotation.MLog;
import com.noinch.mall.springboot.starter.log.aspect.MLogPrintAspect;
import org.springframework.context.annotation.Bean;

/**
 * 日志自动装配
 * */
public class LogAutoConfiguration {
    
    /**
     * {@link MLog} 日志打印 AOP 切面
     */
    @Bean
    public MLogPrintAspect mLogPrintAspect() {
        return new MLogPrintAspect();
    }
}
