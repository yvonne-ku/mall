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



## 支持模块：

---

### mall-idempotent-spring-boot-starter

1. 自定义注解 @Idempotent 实现了幂等性处理。特别针对下单操作和库存锁定操作。


### mall-cache-spring-boot-starter

1. 实现了 StringRedisTemplate 对外接口的简化，
2. 集成 布隆过滤器，分布式锁缓存击穿，空值缓存缓存穿透 的策略。

### mall-convention-spring-boot-starter 和 mall-web-spring-boot-starter

全局异常处理体系：
统一响应格式
设计异常分类体系
设计宏观错误码
进行全局异常拦截处理


## 业务模块：

---

### mall-order

订单创建场景：
1. seata 全局事务，保证本地订单创建、远程购物车清空、远程库存锁定的一致性。
2. RabbitMQ 延迟队列死信交换机：延迟关闭订单处理。com.noinch.mall.biz.order.infrastructure.config.RabbitMQConfig

### mall-product

搜索场景：
1. xxl-job 分布式任务支持 ES 全量同步任务
2. 集成 ES 支持关键词搜索

### mall-pay

支付场景：
1. 策略模式多平台支付
2. 支付成功，rabbitmq 异步回调订单服务，更新订单状态


### 其他

集成第三方接口：
mall-customer-user: 极客人机验证服务。登录，注册场景。
mall-message: 阿里云信息验证服务。登录，注册场景。
mall-pay: 支付宝当面付。