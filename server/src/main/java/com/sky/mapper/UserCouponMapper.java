package com.sky.mapper;

import com.sky.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCouponMapper {
    void insert(UserCoupon userCoupon);
}
