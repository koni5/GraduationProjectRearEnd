package com.sky.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserCoupon implements Serializable {
    //主键
    private Long id;
    //用户id
    private Long userId;
    //优惠券id
    private Long couponId;
    //优惠券数量
    private Long count;
}
