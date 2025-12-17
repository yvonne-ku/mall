

package com.noinch.mall.biz.product.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductSkuDO;

/**
 * 商品 SKU
 */
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {
    
    /**
     * 锁定商品 SKU 库存
     */
    int lockSkuStock(ProductSkuDO productSkuDO);
    
    /**
     * 解锁商品 SKU 库存
     */
    int unlockSkuStock(ProductSkuDO productSkuDO);
    
    /**
     * 通过流式查询的方式获取所有商品 SKU
     */
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(ProductSkuDO.class)
    @Select("SELECT * FROM product_sku WHERE del_flag = '0'")
    void listAllProductSkuStreamQuery(ResultHandler<ProductSkuDO> handler);
}
