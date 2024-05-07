package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {
    //菜品id
    private Long dishId;
    //店铺id
    private Long shopId;
    //口味值
    private String dishFlavor;

}
