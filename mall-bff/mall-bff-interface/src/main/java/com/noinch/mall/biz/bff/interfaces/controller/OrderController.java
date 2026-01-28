package com.noinch.mall.biz.bff.interfaces.controller;


import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.req.adapter.OrderCreateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderResultAdapterRespDTO;
import com.noinch.mall.biz.bff.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制层
 *
 */
@RestController()
@RequiredArgsConstructor
@Tag(name = "订单")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/member/orderList")
    @Operation(description = "订单列表查询")
    public ResultT<OrderResultAdapterRespDTO> listOrder(@RequestParam("page") Integer page,
                                                        @RequestParam("size") Integer size,
                                                        @RequestParam("userId") String userId) {
        return ResultT.success(orderService.listOrder(page, size, userId));
    }

    @GetMapping("/member/orderDetail")
    @Operation(description = "根据订单号查询订单详情")
    public ResultT<OrderAdapterRespDTO> getOrderDetail(@RequestParam("orderId") String orderSn) {
        return ResultT.success(orderService.getOrderDetail(orderSn));
    }

    @PostMapping("/member/addOrder")
    @Operation(description = "订单创建")
//    @SentinelResource(
//            value = CREATE_ORDER_PATH,
//            blockHandler = "createOrderBlockHandlerMethod",
//            blockHandlerClass = CustomBlockHandler.class
//    )
    public ResultT<String> addOrder(@RequestBody OrderCreateAdapterReqDTO requestParam) {
        return ResultT.success(orderService.addOrder(requestParam));
    }

    @GetMapping("/member/delOrder")
    @Operation(description = "订单删除")
    public ResultT<Integer> deleteOrder(@RequestParam("orderId") String orderSn) {
        return ResultT.success(orderService.deleteOrder(orderSn));
    }
}