package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("ShopCartController")
@RequestMapping("/user/shopCart")
@Slf4j
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加的菜品id为:{},口味为:{},所属店铺id为:{}", shoppingCartDTO.getDishId(), shoppingCartDTO.getDishFlavor(), shoppingCartDTO.getShopId());
        shopCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping
    public Result<List<ShoppingCart>> list(Long shopId) {
        List<ShoppingCart> cartList = shopCartService.list(shopId);
        return Result.success(cartList);
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clearAll")
    public Result clearAll(Long shopId){
        Long userId= BaseContext.getCurrentId();
        log.info("用户id:{},删除了店铺id:{}的购物车所有菜品",userId,shopId);
        shopCartService.clearAll(userId,shopId);
        return Result.success();
    }

    /**
     * 添加一条菜品
     * @param shopCartId
     * @return
     */
    @PostMapping("/addOne")
    public Result addOne(Long shopCartId){
        shopCartService.addOne(shopCartId);
        return Result.success();
    }

    /**
     * 减少一条菜品
     * @param shopCartId
     * @return
     */
    @PostMapping("/deleteOne")
    public Result deleteOne(Long shopCartId){
        shopCartService.deleteOne(shopCartId);
        return Result.success();
    }
}
