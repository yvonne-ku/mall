
package com.noinch.mall.biz.bff.interfaces.controller;


import com.noinch.mall.biz.bff.service.CartService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.req.adapter.CartAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.CartAddAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.CartChecksAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.CartDeleteAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.CartDeleteChecksAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.CartUpdateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.CartAdapterRespDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 购物车控制层
 *
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "商品购物车")
public class CartController {

    private final CartService cartService;

    @PostMapping("/member/cartList")
    @Schema(description = "查询用户购物车")
    public ResultT<List<CartAdapterRespDTO>> listAllProductCart(@RequestBody CartAdapterReqDTO requestParam) {
        return ResultT.success(cartService.listAllProductCart(requestParam.getUserId()));
    }

    @PostMapping("/member/addCart")
    @Schema(description = "新增购物车")
//    @SentinelResource(
//            value = ADD_CART_PATH,
//            blockHandler = "addCardBlockHandlerMethod",
//            blockHandlerClass = CustomBlockHandler.class
//    )
    public ResultT<Integer> addProductCard(@RequestBody CartAddAdapterReqDTO requestParam) {
        return ResultT.success(cartService.addProductCard(requestParam));
    }

    @PostMapping("/member/cartEdit")
    @Schema(description = "编辑购物车")
    public ResultT<Integer> updateProductCard(@RequestBody CartUpdateAdapterReqDTO requestParam) {
        return ResultT.success(cartService.updateProductCard(requestParam));
    }

    @PostMapping("/member/cartDel")
    @Schema(description = "删除购物车")
    public ResultT<Integer> deleteProductCard(@RequestBody CartDeleteAdapterReqDTO requestParam) {
        return ResultT.success(cartService.deleteProductCard(requestParam));
    }

    @PostMapping("/member/editCheckAll")
    @Schema(description = "编辑全选购物车")
    public ResultT<Integer> updateChecksProductCard(@RequestBody CartChecksAdapterReqDTO requestParam) {
        return ResultT.success(cartService.updateChecksProductCard(requestParam));
    }

    @PostMapping("/member/delCartChecked")
    @Schema(description = "删除选中购物车")
    public ResultT<Void> deleteChecksProductCard(@RequestBody CartDeleteChecksAdapterReqDTO requestParam) {
        cartService.deleteChecksProductCard(requestParam);
        return ResultT.success();
    }
}
