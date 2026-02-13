

package com.noinch.mall.biz.product.infrastructure.algorithm;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;

/**
 * 基因雪花算法实现，brand_id 作为 product_id 的一部分
 *
 */
public class ProductSkuSnowflakeServiceShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {
    
    @Getter
    private Properties props;
    private int shardingCount;
    private static final String SHARDING_COUNT_KEY = "sharding-count";
    
    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        final String brandId = "brand_id";
        final String productId = "product_id";
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Map<String, Collection<Comparable<Long>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        if (CollUtil.isNotEmpty(columnNameAndShardingValuesMap)) {
            Collection<Comparable<Long>> brandIdCollection = columnNameAndShardingValuesMap.get(brandId);
            Collection<Comparable<Long>> productIdCollection = columnNameAndShardingValuesMap.get(productId);
            // 如果等值查询条件中有 brandId
            if (CollUtil.isNotEmpty(brandIdCollection)) {
                Comparable<?> comparable = brandIdCollection.stream().findFirst().get();
                String tableNameSuffix = String.valueOf(hashShardingValue(comparable) % shardingCount);
                result.add(shardingValue.getLogicTableName() + "_" + tableNameSuffix);
                return result;
            }
            // 如果等值查询条件中有 productId
            else if (CollUtil.isNotEmpty(productIdCollection)) {
                Comparable<Long> comparable = productIdCollection.stream().findFirst().get();
                int tableNameSuffix = SnowflakeIdUtil.parseSnowflakeServiceId(String.valueOf(comparable)).getGene();
                result.add(shardingValue.getLogicTableName() + "_" + tableNameSuffix);
                return result;
            }
        }
        return availableTargetNames;
    }

    
    @Override
    public void init(Properties props) {
        this.props = props;
        shardingCount = getShardingCount(props);
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }

    private int getShardingCount(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_COUNT_KEY), "Sharding count cannot be null.");
        return Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY));
    }
    
    private long hashShardingValue(final Comparable<?> shardingValue) {
        return Math.abs((long) shardingValue.hashCode());
    }
}
