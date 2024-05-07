package com.sky.service;

import com.sky.entity.Dish;

import java.util.List;

public interface DishService {
    List<Dish> list(Long categoryId);
}
