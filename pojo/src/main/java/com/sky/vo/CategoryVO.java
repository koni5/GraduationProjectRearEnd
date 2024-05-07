package com.sky.vo;

import com.sky.entity.CategoryFlavor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class CategoryVO implements Serializable {
    //主键
    private Long id;

    //分类名称
    private String name;

    //分类关联的口味
    private List<CategoryFlavor> flavors = new ArrayList<>();
}
