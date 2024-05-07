package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShopCartService {
    void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list(Long shopId);

    void clearAll(Long userId, Long shopId);

    void addOne(Long shopCartId);

    void deleteOne(Long shopCartId);
}
