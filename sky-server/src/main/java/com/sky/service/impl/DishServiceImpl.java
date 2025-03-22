package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     */
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //属性拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //设置状态禁用
        dish.setStatus(StatusConstant.DISABLE);

        //先插入菜品
        dishMapper.insert(dish);
        //获取菜品的id
        Long id = dish.getCategoryId();

        //插入口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            //设置菜品的id
            flavors.forEach(dishFlavor -> {dishFlavor.setDishId(id);});
            dishFlavorMapper.addFlavors(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dishPageQueryDTO);
        //返回数据
        return new PageResult(page.getTotal(), page.getResult());
    }
}
