package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;


@Mapper
public interface UserMapper {
    /**
     * 插入用户对象
     *
     * @param user
     */
    void insert(User user);

    /**
     * 根据openid查询用户
     *
     * @param openid
     * @return
     */
    User getByOpenid(String openid);

    /**
     * 查询用户折扣
     *
     * @param id
     * @return
     */
    BigDecimal findDiscount(Long id);

    /**
     * 根据用户id查询用户名
     *
     * @param id
     * @return
     */
    @Select("select name from user where id=#{id}")
    String getNameById(Long id);
}
