package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.result.Result;
import com.sky.service.CouponService;
import com.sky.vo.CouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 根据用户id获取优惠券列表
     *
     * @return
     */
    @GetMapping
    public Result<List<CouponVO>> list() {
        List<CouponVO> couponVOList = couponService.list();
        return Result.success(couponVOList);
    }

    /**
     * 根据优惠券id查询折扣值
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<BigDecimal> getDiscount(@PathVariable("id") Long id) {
        BigDecimal discount = couponService.getDiscountById(id);
        return Result.success(discount);
    }

    /**
     * 获取优惠券总数
     * @return
     */
    @GetMapping("/getCount")
    public Result<Long> getCount() {
        Long userId = BaseContext.getCurrentId();
        Long count = couponService.getCount(userId);
        return Result.success(count);
    }
}
