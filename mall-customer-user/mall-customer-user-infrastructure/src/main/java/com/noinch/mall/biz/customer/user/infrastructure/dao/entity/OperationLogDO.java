

package com.noinch.mall.biz.customer.user.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * C 端用户操作日志
*
*/
@Data
@NoArgsConstructor
@TableName("customer_user_operation_log_test")
public class OperationLogDO extends BaseDO {
    
    /**
     * id
     */
    @TableId(type = AUTO)
    private Long id;
    
    /**
     * C端用户ID
     */
    private Long customerUserId;
    
    /**
     * 修改前
     */
    private String beforeContent;
    
    /**
     * 修改后
     */
    private String afterContent;
    
    /**
     * 修改内容
     */
    private String operationContent;
    
    public OperationLogDO(String afterContent) {
        this.afterContent = afterContent;
    }
    
    public OperationLogDO(String beforeContent, String afterContent, String operationContent) {
        this.beforeContent = beforeContent;
        this.afterContent = afterContent;
        this.operationContent = operationContent;
    }
}
