package com.sky.controller.admin;

import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
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
     * 店铺后台订单查询
     *
     * @param page
     * @param pageSize
     * @param status   订单状态 1待接单 2制作中 3已完成 5已取消 6已退款
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> page(int page, int pageSize, Integer status, Long shopId) {
        log.info("店铺查询订单");
        PageResult pageResult = orderService.adminPageQuery(page, pageSize, status, shopId);
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
        orderService.rejectOrder(orderId,rejectReason);
        return Result.success();
    }
}
