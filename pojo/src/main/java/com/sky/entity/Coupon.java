package com.sky.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Coupon implements Serializable {
    //主键
    private Long id;
    //优惠券使用的最低限额
    private BigDecimal minimum;
    //满减多少钱
    private BigDecimal discount;
    //开始期限
    private LocalDate startTime;
    //结束期限
    private LocalDate endTime;
}
