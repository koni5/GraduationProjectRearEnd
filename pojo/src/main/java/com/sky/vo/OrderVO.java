package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {
    //取餐码
    private Long pickupCode;
    //订单菜品信息(用于后台订单详情展示)
    private String orderDishes;
    //用户名
    private String name;
    //订单项总数
    private Integer detailCount;
    //倒计时
    private Long countDown;
    //菜品总数
    private Long dishesNumber;
    //店铺名称
    private String shopName;
    //店铺地址
    private String shopAddress;
    //订单详情
    private List<OrderDetail> orderDetailList;


}
