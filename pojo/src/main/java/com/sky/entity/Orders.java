package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 订单状态 1待付款 2待接单 3制作中 4已完成 5已取消
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer MAKING = 3;
    public static final Integer DONE = 4;
    public static final Integer CANCELLED = 5;

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    //订单号
    private String number;

    //订单状态 1待付款 2待接单 3制作中 4已完成 5已取消
    private Integer status;

    //下单用户id
    private Long userId;

    //下单时间
    private LocalDateTime orderTime;

    //结账时间
    private LocalDateTime checkoutTime;

    //支付方式 1微信，2现金
    private Integer payMethod;

    //支付状态 0未支付 1已支付 2退款
    private Integer payStatus;

    //实收金额
    private BigDecimal amount;

    //应付金额
    private BigDecimal shouldPay;

    //备注
    private String remark;

    //订单取消原因
    private String cancelReason;

    //订单退款原因
    private String rejectionReason;

    //订单取消时间
    private LocalDateTime cancelTime;

    //取餐码
    private Long pickupCode;

    //店铺id
    private Long shopId;

    //优惠券id
    private Long couponId;

}
