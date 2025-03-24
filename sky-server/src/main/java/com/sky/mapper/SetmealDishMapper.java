package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id获取有关联的套餐id
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 插入和套餐关联的菜品
     */
    void insertDishes(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐的id删除对应的数据
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id获取所有的菜品
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    void deleteBySetmealIds(List<Long> setmealIds);
}
