package com.sky.mapper;

import com.sky.entity.CategoryFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryFlavorsMapper {
    /**
     * 根据分类id查询口味list
     * @param categoryId
     * @return
     */
    List<CategoryFlavor> list(Long categoryId);
}
