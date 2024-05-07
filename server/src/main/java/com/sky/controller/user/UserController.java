package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/user/user")
public class UserController {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserService userService;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登录:{}", userLoginDTO.getCode());
        //微信登录
        User user = userService.wxLogin(userLoginDTO);
        //拿到用户id后去生成token
        //为微信用户生成jwt令牌
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        //查找vip折扣值
        BigDecimal discout = userService.findDiscount(user.getId());
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .token(token)
                .vipLevel(user.getVipLevel())
                .couponBalance(user.getCouponBalance())
                .discount(discout)
                .build();
        return Result.success(userLoginVO);
    }

}
