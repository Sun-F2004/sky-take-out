package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
@Slf4j
public class MyTask {

    //创建定时任务
    @Scheduled(cron = "0/5 * * * * *")
    public void executeTask() {
        log.info("定时任务开始执行：{}", LocalDateTime.now());
    }
}
