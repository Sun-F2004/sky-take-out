package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改菜品
     */
    void updateDish(DishDTO dishDTO);

    DishVO getByIdWithFlavor(Long id);

    void updateStatus(Long id, Integer status);

    List<Dish> getByCategoryId(Long categoryId);
}
