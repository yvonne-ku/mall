

package com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WorkId 包装器 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkIdWrapper {
    
    /**
     * 工作ID
     */
    private Long workId;
    
    /**
     * 数据中心ID
     */
    private Long dataCenterId;
}
