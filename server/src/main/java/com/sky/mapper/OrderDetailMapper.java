package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入多条订单明细
     *
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单id查询订单明细
     *
     * @param orderId
     * @return
     */
    List<OrderDetail> getByOrderId(Long orderId,Integer size);

    /**
     * 查询订单菜的总数
     * @param orderId
     * @return
     */
    Long count(Long orderId);
}
