package com.sky.service;

import com.sky.entity.Category;
import com.sky.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    /**
     * 查询分类
     * @return
     */
    List<CategoryVO> list();
}
