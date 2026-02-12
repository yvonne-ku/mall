package com.noinch.mall.springboot.starter.rabbitmq.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@Data
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private String host = "localhost";
    private int port = 5672;
    private String username = "guest";
    private String password = "guest";
    private String virtualHost = "/";
    private Duration connectionTimeout = Duration.ofSeconds(5);

    private final Producer producer = new Producer();
    private final Consumer consumer = new Consumer();

    @Data
    public static class Producer {
        // 确认模式：NONE, MANUAL, CORRELATED (默认异步确认)
        private String confirmType = "correlated";
        // 路由失败退回
        private boolean returns = true;
        // 当 key 不存在时，是否强制投递，true 退回给生产者
        private boolean mandatory = true;
    }

    @Data
    public static class Consumer {
        // 确认模式：auto, manual (生产建议 manual，消费者可以选择消费和退回)
        private String acknowledgeMode = "manual";
        // 预取数量 (生产建议 20-50)
        private int prefetch = 20;
        // 消费者初始并发线程数
        private int concurrency = 5;
        // 消费者最大并发线程数
        private int maxConcurrency = 10;

        // 嵌套：重试逻辑
        private final Consumer.Retry retry = new Consumer.Retry();

        @Data
        public static class Retry {
            private boolean enabled = true;
            private int maxAttempts = 3;
            private Duration initialInterval = Duration.ofSeconds(2);
            private double multiplier = 2.0;
        }
    }
}
