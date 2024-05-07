package com.sky.service.impl;

import com.sky.entity.Category;
import com.sky.entity.CategoryFlavor;
import com.sky.mapper.CategoryFlavorsMapper;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import com.sky.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryFlavorsMapper categoryFlavorsMapper;

    /**
     * 查询分类
     *
     * @return
     */
    @Override
    public List<CategoryVO> list() {
        List<CategoryVO> categoryVOList = new ArrayList<>();
        List<Category> categoryList = categoryMapper.list();
        for (Category category : categoryList) {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            //再根据分类Id去查询对应口味list
            List<CategoryFlavor> flavorList = categoryFlavorsMapper.list(category.getId());
            categoryVO.setFlavors(flavorList);
            categoryVOList.add(categoryVO);
        }
        return categoryVOList;
    }
}
