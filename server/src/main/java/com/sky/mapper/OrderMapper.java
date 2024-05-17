package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 提交订单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 取消订单进行字段更新
     *
     * @param ordersCancelDTO
     */
    void cancelUpdate(OrdersCancelDTO ordersCancelDTO);

    /**
     * 根据id查询订单信息
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders queryById(Long id);

    /**
     * 支付成功后更新订单
     *
     * @param orders
     */
    void payUpdate(Orders orders);

    /**
     * 获取单个订单状态
     *
     * @param id
     * @return
     */
    OrderStatusVO queryStatus(Long id);

    /**
     * 用户查询近期订单
     *
     * @param userId
     * @return
     */
    Page<Orders> pageQuery(Long userId);

    /**
     * 店铺后台查询订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> adminPageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 店铺更新订单状态
     *
     * @param id
     * @param status
     * @param pickupCode
     */
    void updateOrderStatus(Long id, int status, Long pickupCode);

    /**
     * 订单退款更新
     *
     * @param id
     * @param rejectReason
     * @param status
     * @param payStatus
     */
    void refundOrder(Long id, String rejectReason, int status, int payStatus);

    /**
     * 动态查询营业额
     *
     * @param map
     * @return
     */
    Double sumByMap(HashMap<String, Object> map);

    /**
     * 根据动态条件统计订单数量
     *
     * @param map
     * @return
     */
    Integer countByMap(HashMap<Object, Object> map);

    /**
     * 搜索订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> adminNewPageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
    /**
     * 根据订单状态和下单时间查询订单
     *
     * @param orderTime
     * @param status
     * @return
     */
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrderTime(LocalDateTime orderTime, Integer status);

    /**
     * 更新订单信息
     *
     * @param orders
     */
    void update(Orders orders);
}
