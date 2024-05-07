package com.sky.service.impl;

import com.sky.mapper.ShopMapper;
import com.sky.service.ShopService;
import com.sky.vo.ShopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;
    @Override
    public List<ShopVO> list() {
        return shopMapper.list();
    }
}
