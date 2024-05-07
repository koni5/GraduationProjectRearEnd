package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.mapper.CouponMapper;
import com.sky.service.CouponService;
import com.sky.vo.CouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponMapper couponMapper;

    /**
     * 根据用户id获取优惠券列表
     *
     * @return
     */
    @Override
    public List<CouponVO> list() {
        Long userId = BaseContext.getCurrentId();
        List<CouponVO> couponVOList = couponMapper.list(userId);
        return couponVOList;
    }

    /**
     * 根据优惠券id查询折扣值
     *
     * @param id
     * @return
     */
    @Override
    public BigDecimal getDiscountById(Long id) {
        BigDecimal discount = couponMapper.getDiscountById(id);
        return discount;
    }

    /**
     * 获取优惠券总数
     * @param userId
     * @return
     */
    @Override
    public Long getCount(Long userId) {
        Long count= couponMapper.getCount(userId);
        return count;
    }
}
