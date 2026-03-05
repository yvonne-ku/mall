
package com.noinch.mall.springboot.starter.web.config;

import com.noinch.mall.springboot.starter.web.handler.GlobalExceptionHandler;
import com.noinch.mall.springboot.starter.web.interceptor.SeataFeignHandlerInterceptor;
import com.noinch.mall.springboot.starter.web.initialize.InitializeDispatcherServletController;
import com.noinch.mall.springboot.starter.web.initialize.InitializeDispatcherServletHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 组件自动装配
 */
@AutoConfigureAfter(SeataInterceptorAutoConfiguration.class)
public class WebAutoConfiguration implements WebMvcConfigurer {
    
    public final static String INITIALIZE_PATH = "/initialize/dispatcher-servlet";

    private final SeataFeignHandlerInterceptor seataFeignHandlerInterceptor;

    public WebAutoConfiguration(SeataFeignHandlerInterceptor seataFeignHandlerInterceptor) {
        this.seataFeignHandlerInterceptor = seataFeignHandlerInterceptor;
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(seataFeignHandlerInterceptor)
                .addPathPatterns("/**");   // 可根据需要调整拦截路径
    }

    /**
     * 全局异常处理
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler mallGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 预热 dispatchServlet
     * 在 Spring Boot 应用中，DispatcherServlet 默认是懒加载的，
     * 通过预先调用这个端点，可以让 DispatcherServlet 提前完成初始化，避免线上第一笔请求因加载过慢而超时。这种技巧常用于性能优化。
     */
    @Bean
    public InitializeDispatcherServletController initializeDispatcherServletController() {
        return new InitializeDispatcherServletController();
    }

    /**
     * 提供同步的 HTTP 请求能力
     */
    @Bean
    public RestTemplate simpleRestTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }

    @Bean
    public InitializeDispatcherServletHandler initializeDispatcherServletHandler(RestTemplate simpleRestTemplate, ConfigurableEnvironment configurableEnvironment) {
        return new InitializeDispatcherServletHandler(simpleRestTemplate, configurableEnvironment);
    }
}
