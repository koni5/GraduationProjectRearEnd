package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 接单
     *
     * @param orderId
     * @return
     */
    @PutMapping("/receive/{orderId}")
    public Result receiveOrder(@PathVariable("orderId") Long orderId) {
        orderService.receiveOrder(orderId);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param orderId
     * @return
     */
    @PutMapping("/completed/{orderId}")
    public Result completeOrder(@PathVariable("orderId") Long orderId) {
        orderService.completeOrder(orderId);
        return Result.success();
    }

    /**
     * 商家退款
     *
     * @param orderId
     * @return
     */
    @PutMapping("/reject/{orderId}/{rejectReason}")
    public Result rejectOrder(@PathVariable("orderId") Long orderId, @PathVariable("rejectReason") String rejectReason) {
        orderService.rejectOrder(orderId, rejectReason);
        return Result.success();
    }
}
