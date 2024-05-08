package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatusVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 取消订单进行字段更新
     *
     * @param ordersCancelDTO
     */
    void cancelUpdate(OrdersCancelDTO ordersCancelDTO);

    /**
     * 支付成功后更新订单
     *
     * @param orderId
     */
    void payUpdate(Long orderId);

    /**
     * 查询单个订单状态
     *
     * @param orderId
     * @return
     */
    OrderStatusVO queryStatus(Long orderId);

    /**
     * 近期订单查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult pageQuery(int pageNum, int pageSize);

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 店铺后台订单查询
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult adminPageQuery(int page, int pageSize, Integer status, Long shopId);

    /**
     * 商家接单
     * @param orderId
     */
    void receiveOrder(Long orderId);
}
