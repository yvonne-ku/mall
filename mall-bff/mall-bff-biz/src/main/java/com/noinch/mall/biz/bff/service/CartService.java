

package com.noinch.mall.biz.bff.service;



import com.noinch.mall.biz.bff.dto.req.adapter.*;
import com.noinch.mall.biz.bff.dto.resp.adapter.CartAdapterRespDTO;

import java.util.List;

/**
 * 购物车接口
 *
 */
public interface CartService {

    /**
     * 根据用户查询所有购物车选中商品
     *
     * @param userId 用户 ID
     * @return 用户购物车返回数据
     */
    List<CartAdapterRespDTO> listAllProductCart(String userId);

    /**
     * 添加商品到购物车
     *
     * @param requestParam 商品添加购物车请求数据
     * @return 添加购物车数量
     */
    Integer addProductCard(CartAddAdapterReqDTO requestParam);

    /**
     * 修改商品购物车数据
     *
     * @param requestParam 修改商品购物车请求数据
     * @return 修改购物车是否成功
     */
    Integer updateProductCard(CartUpdateAdapterReqDTO requestParam);

    /**
     * 删除商品购物车数据
     *
     * @param requestParam 删除商品购物车请求数据
     * @return 删除购物车是否成功
     */
    Integer deleteProductCard(CartDeleteAdapterReqDTO requestParam);

    /**
     * 编辑全选商品购物车
     *
     * @param requestParam 编辑全选商品购物车请求数据
     * @return 编辑全选商品购物车是否成功
     */
    Integer updateChecksProductCard(CartChecksAdapterReqDTO requestParam);

    /**
     * 删除选中商品购物车
     *
     * @param requestParam 删除选中商品购物车请求数据
     */
    void deleteChecksProductCard(CartDeleteChecksAdapterReqDTO requestParam);
}
