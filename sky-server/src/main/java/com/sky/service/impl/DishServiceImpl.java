package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     */
    @Override
    @Transactional //多表操作使用事务保证一致性
    public void saveWithFlavor(DishDTO dishDTO) {
        //属性拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //设置状态禁用
        dish.setStatus(StatusConstant.DISABLE);

        //先插入菜品
        dishMapper.insert(dish);
        //获取菜品的id
        Long dishId = dish.getId();

        //插入口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            //设置菜品的id
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
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

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            //如果菜品在起售则不能删除
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //如果批量删除的菜品中有被套餐关联的也不能删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds != null && !setmealIds.isEmpty())
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        //删除菜品以及其对应的口味
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        //属性拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //更新菜品属性
        dishMapper.update(dish);

        Long id = dish.getId();
        //删除原有绑定的口味
        dishFlavorMapper.deleteByDishId(id);
        //增加口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            dishFlavorMapper.addFlavors(flavors);
        }
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        DishVO dishVO = new DishVO();

        //获取菜品信息
        Dish dish = dishMapper.getById(id);
        BeanUtils.copyProperties(dish, dishVO);
        //获取口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        if(flavors != null && !flavors.isEmpty()) dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Dish dish = Dish.builder().id(id).status(status).build();
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }
}
