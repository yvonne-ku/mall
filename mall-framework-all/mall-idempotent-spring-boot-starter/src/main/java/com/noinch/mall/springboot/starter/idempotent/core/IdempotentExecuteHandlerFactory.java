
package com.noinch.mall.springboot.starter.idempotent.core;

import com.noinch.mall.springboot.starter.base.ApplicationContextHolder;
import com.noinch.mall.springboot.starter.idempotent.core.param.IdempotentParamService;
import com.noinch.mall.springboot.starter.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import com.noinch.mall.springboot.starter.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import com.noinch.mall.springboot.starter.idempotent.core.token.service.IdempotentTokenService;
import com.noinch.mall.springboot.starter.idempotent.enums.IdempotentSceneEnum;
import com.noinch.mall.springboot.starter.idempotent.enums.IdempotentTypeEnum;

/**
 * 幂等执行处理器工厂
 * <p>
 * Q：可能会有同学有疑问：这里为什么要采用简单工厂模式？策略模式不行么？
 * A：策略模式同样可以达到获取真正幂等处理器功能。但是从设计模式语义来说，简单工厂模式会更合适
 *
 */
public final class IdempotentExecuteHandlerFactory {
    
    /**
     * 获取幂等执行处理器
     *
     * @param scene 指定幂等验证场景类型
     * @param type  指定幂等处理类型
     * @return 幂等执行处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type) {
        IdempotentExecuteHandler result = null;
        switch (scene) {
            case RESTAPI:
                switch (type) {
                    case PARAM:
                        result = ApplicationContextHolder.getBean(IdempotentParamService.class);
                        break;
                    case TOKEN:
                        result = ApplicationContextHolder.getBean(IdempotentTokenService.class);
                        break;
                    case SPEL:
                        result = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
                        break;
                    default:
                        break;
                }
                break;
            case MQ:
                result = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
                break;
            default:
                break;
        }
        return result;
    }
}
