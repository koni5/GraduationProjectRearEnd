package com.sky.controller.orderMachine;

import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.vo.CategoryVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController("MachineCategoryController")
@RequestMapping("/machine/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 查询分类
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> list() {
        List<CategoryVO> list = categoryService.list();
        return Result.success(list);
    }
}
