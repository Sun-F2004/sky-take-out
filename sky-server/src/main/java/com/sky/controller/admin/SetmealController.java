package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关操作")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询：{}", setmealPageQueryDTO);
        return Result.success(setmealService.pageQuery(setmealPageQueryDTO));
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    public Result<String> updateStatus(@PathVariable Integer status, Long id){
        setmealService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        return Result.success(setmealService.getById(id));
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result<String> deleteByIds(@RequestParam List<Long> ids){
        setmealService.deleteByIds(ids);
        return Result.success();
    }
}
