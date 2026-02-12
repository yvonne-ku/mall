package com.noinch.mall.springboot.starter.rabbitmq.config;


import com.noinch.mall.springboot.starter.rabbitmq.config.properties.RabbitMQProperties;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * RabbitMQ 自动装配
 *
 */
@EnableConfigurationProperties(RabbitMQProperties.class)
@ConditionalOnClass({ConnectionFactory.class, RabbitTemplate.class})
public class RabbitMQAutoConfiguration {

    // 序列化 Bean：统一使用 JSON
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 1. 基础连接工厂：将你的 host, port, 确认模式等注入
    @Bean
    @ConditionalOnMissingBean(name = "customConnectionFactory")
    public ConnectionFactory customConnectionFactory(RabbitMQProperties properties) {
        CachingConnectionFactory factory = new CachingConnectionFactory();

        // 基础配置
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUsername(properties.getUsername());
        factory.setPassword(properties.getPassword());
        factory.setVirtualHost(properties.getVirtualHost());
        // 连接超时（Duration → 毫秒）
        if (properties.getConnectionTimeout() != null) {
            factory.setConnectionTimeout((int) properties.getConnectionTimeout().toMillis());
        }
        // 对应你的 Producer 内部类配置
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.valueOf(properties.getProducer().getConfirmType().toUpperCase()));
        factory.setPublisherReturns(properties.getProducer().isReturns());

        return factory;
    }

    // 2. 消息发送模板：配置 JSON 序列化和强制投递
    @Bean
    @ConditionalOnMissingBean(name = "customRabbitTemplate")
    public RabbitTemplate customRabbitTemplate(@Qualifier("customConnectionFactory") ConnectionFactory customConnectionFactory, RabbitMQProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(customConnectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        rabbitTemplate.setMandatory(properties.getProducer().isMandatory());
        return rabbitTemplate;
    }

    // 3. 消费者容器工厂：这是 @RabbitListener 的底层支持
    @Bean
    @ConditionalOnMissingBean(name = "customRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory customRabbitListenerContainerFactory(@Qualifier("customConnectionFactory") ConnectionFactory customConnectionFactory, RabbitMQProperties properties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(customConnectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());

        // 对应 Consumer 内部类配置
        RabbitMQProperties.Consumer consumer = properties.getConsumer();
        factory.setAcknowledgeMode(AcknowledgeMode.valueOf(consumer.getAcknowledgeMode().toUpperCase()));
        factory.setPrefetchCount(consumer.getPrefetch());
        factory.setConcurrentConsumers(consumer.getConcurrency());
        factory.setMaxConcurrentConsumers(consumer.getMaxConcurrency());

        // 对应你的 Consumer.Retry 重试配置
        if (consumer.getRetry().isEnabled()) {
            // 这里通常配合 RetryInterceptor 使用，或者简单开启 Retry 模板
            factory.setAdviceChain(
                    RetryInterceptorBuilder.stateless()
                            .maxAttempts(consumer.getRetry().getMaxAttempts())
                            .backOffOptions(
                                    consumer.getRetry().getInitialInterval().toMillis(),
                                    consumer.getRetry().getMultiplier(),
                                    consumer.getRetry().getInitialInterval().toMillis() * 5 // 最大间隔
                            )
                            .build()
            );
        }
        return factory;
    }

    // 4. 管理组件：用于自动创建 Queue, Exchange
    @Bean
    @ConditionalOnMissingBean(name = "customAmqpAdmin")
    public AmqpAdmin customAmqpAdmin(@Qualifier("customConnectionFactory") ConnectionFactory customConnectionFactory) {
        return new RabbitAdmin(customConnectionFactory);
    }
}