package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopVO implements Serializable {
    //主键
    private Long id;
    //店名
    private String name;
    //地址
    private String address;
    //电话
    private String phone;
}
