package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    /**
     * 提交订单
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 取消订单进行字段更新
     * @param ordersCancelDTO
     */
    void cancelUpdate(OrdersCancelDTO ordersCancelDTO);

    /**
     * 根据id查询订单信息
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders queryById(Long id);

    /**
     * 支付成功后更新订单
     * @param orders
     */
    void payUpdate(Orders orders);

    /**
     * 获取单个订单状态
     * @param id
     * @return
     */
    OrderStatusVO queryStatus(Long id);

    /**
     * 用户查询近期订单
     * @param userId
     * @return
     */
    Page<Orders> pageQuery(Long userId);

    /**
     * 店铺后台查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> adminPageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
}