package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询
     */
    Page<Dish> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id获取菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id删除菜品
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id批量删除菜品
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改菜品
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 使用连接表来根据套餐id获取关联的菜品
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id " +
            "where b.setmeal_id = #{id}")
    List<Dish> getBySetmealId(Long id);
}
