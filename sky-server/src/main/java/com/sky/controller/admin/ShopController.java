package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.sky.constant.ShopConstant.SHOP_STATUS;


@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关操作")
public class ShopController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result<String> setStatus(@PathVariable Integer status){
        log.info("设置营业状态：{}", status);
        redisTemplate.opsForValue().set(SHOP_STATUS, status.toString());
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus() {
        Integer status =  Integer
                .valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(SHOP_STATUS)));
        log.info("当前营业状态：{}", status);
        return Result.success(status);
    }
}
