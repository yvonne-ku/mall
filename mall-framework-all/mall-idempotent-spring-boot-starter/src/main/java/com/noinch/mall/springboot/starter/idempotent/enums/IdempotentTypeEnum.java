
package com.noinch.mall.springboot.starter.idempotent.enums;

/**
 * 幂等验证类型枚举
 *
 */
public enum IdempotentTypeEnum {
    
    /**
     * 基于 Token 方式验证
     * 最常见的方式。
     * 前端在进入某个页面时，先向后端申请一个唯一的 Token（UUID）。
     * 后端将 Token 存入缓存中，并返回给前端。
     * 前端提交表单或请求时，必须在请求头或参数中带上这个 Token。
     * 后端收到请求后，判定 Token 是否存在：
        * 存在：删除该 Token，执行业务
        * 不存在：判定为重复请求，拒绝。
     */
    TOKEN,
    
    /**
     * 基于方法参数方式验证
     * 通过 AOP 拦截请求，根据请求中唯一标识（如订单号、支付单号等）生成幂等键，存入缓存，设置过期时间。
     * 当相同幂等键再次到达时，缓存中已存在，拒绝执行。
     */
    PARAM,
    
    /**
     * 基于 SpEL 表达式方式验证
     * 利用 SpEL 动态计算幂等键，比 PARAM 方式更灵活。可以直接在注解中编写 SpEL 表达式
     * 校验方式同 PARAM
     */
    SPEL
}
