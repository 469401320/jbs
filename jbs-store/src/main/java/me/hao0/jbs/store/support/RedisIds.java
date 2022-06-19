package me.hao0.jbs.store.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisIds {

    @Autowired
    private StringRedisTemplate redis;


    public Long generate(String idGeneratorKey){
        return redis.opsForValue().increment(idGeneratorKey, 1);
    }
}
