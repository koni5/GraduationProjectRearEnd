package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //口味名称
    private String name;

    //分类id
    private Long CategoryId;

    //口味数据list
    private String value;

}
