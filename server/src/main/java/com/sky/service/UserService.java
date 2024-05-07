package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

import java.math.BigDecimal;

public interface UserService {
    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);

    /**
     * 查找用户会员等级对应的折扣值
     * @param id
     * @return
     */
    BigDecimal findDiscount(Long id);
}
