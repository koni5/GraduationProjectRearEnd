package com.sky.dto;

import com.sky.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdersDTO implements Serializable {

    //实收金额
    private BigDecimal amount;

    //付款方式
    private Integer payMethod;

    //备注
    private String remark;

    //优惠券id
    private Long couponId;

    //店铺id
    private Long shopId;

}
