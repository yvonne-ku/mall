

package com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid;

import cn.hutool.core.collection.CollUtil;
import com.noinch.mall.springboot.starter.base.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Redis 获取雪花 WorkId
 * */
@Slf4j
public class LocalRedisWorkIdChoose implements WorkIdChooser {
    
    private final RedisTemplate stringRedisTemplate;
    
    public LocalRedisWorkIdChoose() {
        this.stringRedisTemplate = ApplicationContextHolder.getBean(StringRedisTemplate.class);
    }
    
    @Override
    public WorkIdWrapper chooseWorkId() {
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/chooseWorkIdLua.lua")));
        List<Long> luaResultList = null;
        try {
            redisScript.setResultType(List.class);
            luaResultList = (ArrayList) this.stringRedisTemplate.execute(redisScript, null);
        } catch (Exception ex) {
            log.error("Redis Lua 脚本获取 WorkId 失败", ex);
        }
        return CollUtil.isNotEmpty(luaResultList) ? new WorkIdWrapper(luaResultList.get(0), luaResultList.get(1)) : new RandomWorkIdChoose().chooseWorkId();
    }

    @Override
    public String getName() {
        return "RedisWorkIdChooser";
    }

}
