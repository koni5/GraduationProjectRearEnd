package com.sky.controller.user;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatusVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Slf4j
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单,参数为:{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 取消订单进行字段更新
     *
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancelUpdate(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 支付成功后更新订单
     *
     * @param orderId
     * @return
     */
    @PutMapping("/pay")
    public Result pay(Long orderId) {
        orderService.payUpdate(orderId);
        return Result.success();
    }

    /**
     * 查询单个订单状态
     *
     * @param orderId
     * @return
     */
    @GetMapping("/status")
    public Result<OrderStatusVO> getOrderStatus(Long orderId) {
        OrderStatusVO orderStatusVO = orderService.queryStatus(orderId);
        return Result.success(orderStatusVO);
    }

    /**
     * 近期订单查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/recentOrders")
    public Result<PageResult> page(int pageNum, int pageSize) {
        PageResult pageResult = orderService.pageQuery(pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> detail(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

}
