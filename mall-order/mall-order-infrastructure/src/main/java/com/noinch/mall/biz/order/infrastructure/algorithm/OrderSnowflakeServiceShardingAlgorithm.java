

package com.noinch.mall.biz.order.infrastructure.algorithm;

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
 * 基因雪花算法实现，customer_user_id 作为 order_sn 的一部分
 *
 */
public class OrderSnowflakeServiceShardingAlgorithm implements ComplexKeysShardingAlgorithm {
    
    @Getter
    private Properties props;
    private int shardingCount;
    private static final String SHARDING_COUNT_KEY = "sharding-count";
    
    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        String customerUserId = "customer_user_id";
        String orderSn = "order_sn";
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        if (CollUtil.isNotEmpty(columnNameAndShardingValuesMap)) {
            Collection<Comparable<?>> customerUserIdCollection = columnNameAndShardingValuesMap.get(customerUserId);
            Collection<Comparable<?>> orderSnCollection = columnNameAndShardingValuesMap.get(orderSn);
            if (CollUtil.isNotEmpty(customerUserIdCollection)) {
                Comparable<?> comparable = customerUserIdCollection.stream().findFirst().get();
                String tableNameSuffix = String.valueOf(hashShardingValue(comparable) % shardingCount);
                result.add(shardingValue.getLogicTableName() + "_" + tableNameSuffix);
                return result;
            }
            if (CollUtil.isNotEmpty(orderSnCollection)) {
                Comparable<?> comparable = orderSnCollection.stream().findFirst().get();
                int tableNameSuffix = SnowflakeIdUtil.parseSnowflakeServiceId((String) comparable).getGene();
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
