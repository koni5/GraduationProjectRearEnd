package com.sky.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class OrderStatusVO implements Serializable {
    //订单状态码
    private Integer status;
    //取餐码(只有接单后才会有)
    private Long pickupCode;
}
