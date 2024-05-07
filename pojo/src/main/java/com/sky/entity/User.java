package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键
    private Long id;

    //微信用户唯一标识
    private String openid;

    //昵称
    private String name;

    //积分数量
    private Integer integralNum;

    //账户余额
    private BigDecimal balance;

    //账户等级
    private String vipLevel;

    //优惠券数量
    private Integer couponBalance;

}
