package com.sky.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
//注意加上@Data注解,不然mybatis无法将数据库字段赋值给对象
@Data
public class CouponVO implements Serializable {
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
    //优惠券数量
    private Integer count;
}
