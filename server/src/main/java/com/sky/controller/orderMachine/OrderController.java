package com.sky.controller.orderMachine;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController("machineOrderController")
@RequestMapping("/machine/order")
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
        OrderSubmitVO orderSubmitVO = orderService.submitOrder_machine(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}
