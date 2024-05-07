package com.sky.mapper;

import com.sky.vo.ShopVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShopMapper {
    /**
     * 查询店铺列表
     * @return
     */
    List<ShopVO> list();

    /**
     * 根据id查询店铺信息
     * @param id
     * @return
     */
    @Select("select * from shop where id=#{id}")
    ShopVO queryById(Long id);
}
