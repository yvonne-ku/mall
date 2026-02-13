

package com.noinch.mall.biz.message.infrastructure.algorithm;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;

import java.util.*;

/**
 * 自定义分片算法
 *
 */
public final class SnowflakeDateShardingAlgorithm implements ComplexKeysShardingAlgorithm<Date> {
    
    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        final String sendTime = "create_time";
        final String messageSendId = "msg_id";
        String logicTableName = shardingValue.getLogicTableName();
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        Map<String, Range<Comparable<?>>> columnNameAndRangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
        // 等值查询条件
        if (CollUtil.isNotEmpty(columnNameAndShardingValuesMap)) {
            Collection<Comparable<?>> sendTimeCollection = columnNameAndShardingValuesMap.get(sendTime);
            Collection<Comparable<?>> messageSendIdCollection = columnNameAndShardingValuesMap.get(messageSendId);
            // 等值查询条件有 create_time
            if (CollUtil.isNotEmpty(sendTimeCollection)) {
                Comparable<?> comparable = sendTimeCollection.stream().findFirst().get();
                String actualTable = ShardModel.quarterlyModel(logicTableName, (Date) comparable);
                result.add(actualTable);
                return result;
            }
            // 等值查询条件有 msg_id
            if (CollUtil.isNotEmpty(messageSendIdCollection)) {
                Comparable<?> comparable = messageSendIdCollection.stream().findFirst().get();
                String actualTable = ShardModel.quarterlyModel(logicTableName, new Date(SnowflakeIdUtil.parseSnowflakeId(((Long) comparable)).getTimestamp()));
                result.add(actualTable);
                return result;
            }
        }
        // 范围查询条件
        if (CollUtil.isNotEmpty(columnNameAndRangeValuesMap)) {
            Range<Comparable<?>> sendTimeRange = columnNameAndRangeValuesMap.get(sendTime);
            if (sendTimeRange != null) {
                List<String> actualTables = ShardModel.calculateRange(logicTableName, (Date) sendTimeRange.lowerEndpoint(), (Date) sendTimeRange.upperEndpoint());
                result.addAll(actualTables);
                return result;
            }
        }
        return availableTargetNames;
    }
    
    @Override
    public void init(Properties properties) {
        
    }
    
    @Override
    public String getType() {
        return "CLASS_BASED";
    }
}
