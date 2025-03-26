package com.sky.controller.user;

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


@RestController("userController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关操作")
public class ShopController {

    @Autowired
    private StringRedisTemplate redisTemplate; //使用字符串的redis模板，防止乱码占用大量内存

    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus() {
        Integer status =  Integer
                .valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(SHOP_STATUS)));
        log.info("当前营业状态：{}", status);
        return Result.success(status);
    }
}
