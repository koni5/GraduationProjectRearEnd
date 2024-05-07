package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
    //令牌
    private String token;
    //账户等级
    private String vipLevel;
    //优惠券剩余数量
    private Integer couponBalance;
    //会员折扣值
    private BigDecimal discount;

}
