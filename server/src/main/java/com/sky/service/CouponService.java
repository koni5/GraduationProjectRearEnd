package com.sky.service;

import com.sky.vo.CouponVO;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    /**
     * 根据用户id获取优惠券列表
     * @return
     */
    List<CouponVO> list();

    /**
     * 根据优惠券id查询折扣值
     * @param id
     * @return
     */
    BigDecimal getDiscountById(Long id);

    /**
     * 获取优惠券总数
     * @param userId
     * @return
     */
    Long getCount(Long userId);
}
