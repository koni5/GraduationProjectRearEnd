package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.ShopCartMapper;
import com.sky.service.ShopCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCartServiceImpl implements ShopCartService {
    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车中的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> list = shopCartMapper.list(shoppingCart);
        //如果已存在
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shopCartMapper.updateNumberById(cart);
        } else {
            //如果不存在
            Long dishId = shoppingCartDTO.getDishId();
            Dish dish = dishMapper.getById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setCost(dish.getCost());
            shoppingCart.setNumber(1);
            shopCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查询购物车
     *
     * @param shopId
     * @return
     */
    @Override
    public List<ShoppingCart> list(Long shopId) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setShopId(shopId);
        List<ShoppingCart> cartList = shopCartMapper.list(shoppingCart);
        return cartList;
    }

    /**
     * 清空购物车
     *
     * @param userId
     * @param shopId
     */
    @Override
    public void clearAll(Long userId, Long shopId) {
        shopCartMapper.deleteAll(userId, shopId);
    }

    @Override
    public void addOne(Long shopCartId) {
        shopCartMapper.addOne(shopCartId);
    }

    @Override
    public void deleteOne(Long shopCartId) {
        ShoppingCart shoppingCart = shopCartMapper.selectById(shopCartId);
        int number = shoppingCart.getNumber();
        if (number > 1) {
            shopCartMapper.deleteOne(shopCartId);
        }else{
            shopCartMapper.deleteById(shopCartId);
        }
    }

}
