package com.noinch.mall.test.framework.cache;

import com.google.common.collect.Lists;
import com.noinch.mall.springboot.starter.cache.DistributedCache;
import com.noinch.mall.springboot.starter.cache.core.CacheLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CacheTests {

    @Autowired
    private DistributedCache distributedCache;

    @Autowired
    private RBloomFilter<String> bloomFilter;

    @BeforeEach
    public void beforeEach() {
        bloomFilter.tryInit(30000, 0.03);
    }

    @AfterEach
    public void afterEach() {
        distributedCache.delete("test");
        bloomFilter.delete();
    }

    @Test
    public void testBloomFilter() {
        AtomicInteger loadCounter = new AtomicInteger(0);
        CacheLoader<String> loader = () -> {
            loadCounter.incrementAndGet();
            return null;
        };

        // 检验 BloomFilter 的拦截效果
        assertThat(bloomFilter.contains("test")).isFalse();
        distributedCache.safeGet("test", String.class, loader, 5000L, bloomFilter);
        assertThat(loadCounter.get()).isEqualTo(0);

        // 检验 BloomFilter 的放行效果
        distributedCache.safePut("test", "test_value", 5000L, bloomFilter);
        assertThat(bloomFilter.contains("test")).isTrue();
        distributedCache.delete("test");
        distributedCache.safeGet("test", String.class, loader, 5000L, bloomFilter);
        assertThat(loadCounter.get()).isEqualTo(1);
    }

    @Test
    public void testCacheLoader() {
        AtomicInteger loadCounter = new AtomicInteger(0);
        CacheLoader<String> loader = () -> {
            loadCounter.incrementAndGet();
            return "test_value";
        };

        // 检验 CacheLoader 的加载效果
        String actualBefore = distributedCache.get("test", String.class);
        String actualRes = distributedCache.get("test", String.class, loader, 5000L);
        assertThat(actualBefore).isEqualTo(null);
        assertThat(actualRes).isEqualTo("test_value");
        assertThat(loadCounter.get()).isEqualTo(1);

        // 检验 CacheLoader 更新到 cache 中的效果
        String actualAfter = distributedCache.get("test", String.class);
        assertThat(actualAfter).isEqualTo("test_value");
    }

    @Test
    public void testPutIfAllAbsent() {
        List<String> keys = Lists.newArrayList("name", "age");
        Boolean result = distributedCache.putIfAllAbsent(keys);
        assertThat(result).isTrue();

        keys.forEach(each -> {
            String name = distributedCache.get("name", String.class);
            assertThat(name).isEqualTo("default");
        });

        Boolean resultFalse = distributedCache.putIfAllAbsent(keys);
        assertThat(resultFalse).isFalse();
    }
}
