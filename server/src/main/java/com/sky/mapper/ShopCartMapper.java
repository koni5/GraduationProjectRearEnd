package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopCartMapper {

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart cart);

    void insert(ShoppingCart shoppingCart);

    void deleteAll(Long userId, Long shopId);

    @Update("update shopping_cart set number=number+1 where id=#{id}")
    void addOne(Long id);

    @Update("update shopping_cart set number=number-1 where id=#{id}")
    void deleteOne(Long id);

    @Select("select * from shopping_cart where id=#{id}")
    ShoppingCart selectById(Long shopCartId);

    @Delete("delete from shopping_cart where id=#{id}")
    void deleteById(Long id);
}
