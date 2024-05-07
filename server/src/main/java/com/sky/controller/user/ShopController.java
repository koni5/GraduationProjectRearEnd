package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.ShopService;
import com.sky.vo.ShopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 查询所有商铺
     * @return
     */
    @GetMapping
    public Result<List<ShopVO>> getShops(){
        List<ShopVO> shopVOList= shopService.list();
        return Result.success(shopVOList);
    }

}
