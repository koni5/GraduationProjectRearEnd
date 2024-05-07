package com.sky.vo;

import java.io.Serializable;
import java.util.List;

public class DishFlavorVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //口味名称
    private String name;

    //分类id
    private Long CategoryId;

    //口味数据list
    private List value;
}
