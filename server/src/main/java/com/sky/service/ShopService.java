package com.sky.service;

import com.sky.vo.ShopVO;

import java.util.List;

public interface ShopService {
    /**
     * 查找所有店铺
     */
    List<ShopVO> list();
}
