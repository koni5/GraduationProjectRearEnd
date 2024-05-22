package com.sky.controller.orderMachine;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController("machineShopCartController")
@RequestMapping("/machine/shoppingCart")
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
}
