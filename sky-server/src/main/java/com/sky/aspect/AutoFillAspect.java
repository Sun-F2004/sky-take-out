package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointcut(){ }

    @Before("pointcut()")
    public void autoFill(JoinPoint joinPoint) throws Exception {
        log.info("开始进行公共字段的填充...");
        //使用反射来获取方法中注解的数据库操作类型和传入的参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //获取签名对象

        //获取注解的操作类型
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();

        //获取方法的参数，约定：所有对应方法的第一个参数都是要操作的数据库对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) return;
        Object entity = args[0];

        //设置要进行自动填充的值
        LocalDateTime time = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //使用反射来进行填充
        if (operationType == OperationType.INSERT || operationType == OperationType.UPDATE){
            Method updateTime = entity.getClass().
                    getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method updateUser = entity.getClass().
                    getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            //更新
            updateTime.invoke(entity, time);
            updateUser.invoke(entity, currentId);
            //插入操作额外更新
            if(operationType == OperationType.INSERT) {
                Method createTime = entity.getClass().
                        getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method createUser = entity.getClass().
                        getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                //更新
                createTime.invoke(entity, time);
                createUser.invoke(entity, currentId);
            }
        }
    }
}
