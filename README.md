### 🎉 Mall 商城 - 练习项目(magestack.cn)

---

参照商城系统原型，实现用户、消息、商品、购物车、订单、支付等业务模块，实现 framework-all、bff、aggregation、gateway 等支持模块。

采用领域驱动设计（DDD）模型四层架构进行设计开发：
1. 界面层（User Interface Layer）：负责处理用户输入和输出，与应用层进行交互。
2. 应用层（Application Layer）：负责处理应用程序的业务逻辑，协调领域层的模型和服务。
3. 领域层（Domain Layer）：负责定义业务领域的模型、规则和业务逻辑。
4. 基础设施层（Infrastructure Layer）：负责实现与外部系统的交互，如数据库、消息队列、缓存等。


### 🐤 服务列表

---

|    | 模块名称                | 服务名称         | 访问地址                                           |
|----|---------------------|--------------|------------------------------------------------|
| 1  | mall-gateway        | 外部网关         | [http://localhost:8000](http://localhost:8000) |
| 2  | mall-message        | 消息发送         | [http://localhost:8001](http://localhost:8001) |
| 2  | mall-customer-user  | 用户服务         | [http://localhost:8002](http://localhost:8002) |
| 4  | mall-product        | 商品服务         | [http://localhost:8003](http://localhost:8003) |
| 5  | mall-product-job    | 商品 Job 服务    | [http://localhost:9001](http://localhost:9001) |
| 6  | mall-cart           | 购物车服务        | [http://localhost:8004](http://localhost:8004) |
| 7  | mall-order          | 订单服务         | [http://localhost:8005](http://localhost:8005) |
| 8  | mall-pay            | 支付服务         | [http://localhost:8006](http://localhost:8006) |
| 9  | mall-basic-data     | 基础数据服务       | [http://localhost:8007](http://localhost:8007) |
| 10 | mall-bff            | BFF 层        | [http://localhost:8008](http://localhost:8008) |


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