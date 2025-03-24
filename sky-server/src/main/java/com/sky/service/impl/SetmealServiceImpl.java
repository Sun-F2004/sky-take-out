package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        //存入套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        //设置状态禁用
        setmeal.setStatus(StatusConstant.DISABLE);

        //获取存入的套餐id
        Long setmealId = setmeal.getId();
        //存入包含的菜品
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        dishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertDishes(dishes);
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> setmeals = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(setmeals.getTotal(), setmeals.getResult());
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if(Objects.equals(status, StatusConstant.ENABLE)){
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if(dishList != null && !dishList.isEmpty()){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE.equals(dish.getStatus())){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //更新套餐数据
        setmealMapper.update(setmeal);
        //删除之前绑定的setmealDish
        Long setmealId = setmeal.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        //插入新的setmealDish
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertDishes(setmealDTO.getSetmealDishes());
    }

    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        //查询套餐数据并注入
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        //查询套餐的菜品数据并注入
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        //若套餐起售则无法删除
        ids.forEach(id -> {
            if(Objects.equals(setmealMapper.getById(id).getStatus(), StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        });
        setmealMapper.deleteByIds(ids);
        setmealDishMapper.deleteBySetmealIds(ids);
    }
}
