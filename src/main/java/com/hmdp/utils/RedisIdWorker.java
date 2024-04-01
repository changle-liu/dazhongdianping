package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {

    private StringRedisTemplate stringRedisTemplate;

    //开始时间戳
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    //序列号时间戳部分左移位数
    private static final int COUNT_BITS = 32;

    public long nextId(String keyPrefix){
        //1. 生成时间戳
        long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - BEGIN_TIMESTAMP;

        //2. 生成序列号
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        //3. 拼接（或运算效率更高）
        return timestamp<<COUNT_BITS | count;
    }
}
