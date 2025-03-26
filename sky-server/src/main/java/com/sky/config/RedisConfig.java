package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建redisTemplate对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置key的序列化器，防止乱码
        redisTemplate.setKeySerializer(RedisSerializer.string());
        return redisTemplate;
    }
}
