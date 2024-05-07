package com.sky.mapper;

import com.sky.vo.CouponVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CouponMapper {
    /**
     * 根据用户id获取优惠券列表
     *
     * @param userId
     * @return
     */
    List<CouponVO> list(Long userId);

    /**
     * 查询优惠券数量
     *
     * @param userId
     * @param couponId
     * @return
     */
    Long queryCount(Long userId, Long couponId);

    /**
     * 更新优惠券数量
     *
     * @param userId
     * @param couponId
     */
    void updateCount(Long userId, Long couponId);

    /**
     * 优惠券耗尽时删除对应用户拥有得优惠券信息
     *
     * @param userId
     * @param couponId
     */
    void delete(Long userId, Long couponId);

    /**
     * 查询关系表中是否有对应项
     *
     * @param userId
     * @param couponId
     * @return
     */
    Long queryUserCouponId(Long userId, Long couponId);

    /**
     * 增加优惠券数量
     *
     * @param userId
     * @param couponId
     */
    void addCount(Long userId, Long couponId);

    /**
     * 根据优惠券id查询折扣值
     *
     * @param id
     * @return
     */
    @Select("select discount from coupon where id=#{id}")
    BigDecimal getDiscountById(Long id);

    /**
     * 获取用户优惠券总数
     *
     * @param userId
     * @return
     */
    @Select("select SUM(count) from user_coupon where user_id=#{userId}")
    Long getCount(Long userId);
}
