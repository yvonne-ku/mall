
1 mall-cache-spring-boot-starter
===
实现了 StringRedisTemplate 的封装，通过 BloomFilter 解决了缓存穿透的问题。

2 mall-distributedid-spring-boot-starter
===
实现了 Twitter 的 Snowflake 算法的工具封装，实现了分布式 ID 生成器，支持融合 serviceId。

3 mall-common-spring-boot-starter
===
主要是实现了线程池的封装，提供了拒绝策略代理类、饥饿线程池实现类等。

4 mall-log-spring-boot-starter
===
实现了切面入参出参执行时间日志打印。

mall-base-spring-boot-starter
===
暂时不看 base，因为涉及到 spring 底层了

mall-designpattern-spring-boot-starter
===
暂时不看 designpattern，因为直接看组件太抽象了，等看到业务代码的时候再看组件吧

mall-flow-monitor-agent
===
暂时不看

mall-mybatisplus-spring-boot-starter
===
暂时不看，因为这个东西目前只要会用就好
实现了 MyBatis-Plus 的封装，提供了分页插件、SQL 性能分析。

mall-web-spring-boot-starter
===
这个主要涉及了 servlet 相关的包，我暂时不打算看。晚一点在安排。

mall-convention-spring-boot-starter
===
为什么不能把 web 直接移到 convention 中：
因为 RPC 服务使用 convention 模块就行，但不依赖 web 模块

mall-idempotent-spring-boot-starter
===
太多东西了，懒得看







支持模块：
===

### mall-bff
===
一个前端请求可能涉及后端多个模块，bff 进行后端模块的分别请求与数据整合，返回给前端需要的数据格式

业务模块：
===

### mall-order
===
订单创建场景：
1. Spring 事件机制：订单创建，购物车清空，库存锁定，延迟关闭订单消息
2. 全局回滚
3. RabbitMQ 延迟队列 死信交换机：延迟关闭订单处理

支付场景：
1. 策略模式多平台支付
2. 支付回调，RabbitMQ 异步通知订单模块更新订单状态