package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.*;

/**
 * 自定义注解，用于判断方法是否需要自动填充公共字段
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoFill {
    //操作的类型
    OperationType value();
}
