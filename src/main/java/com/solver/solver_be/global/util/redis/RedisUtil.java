package com.solver.solver_be.global.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate template;

    // 1. Get RedisData
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    // 2. Exist RedisData
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    // 3. Set RedisData And Expire
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // 4. Delete RedisData
    public void deleteData(String key) {
        template.delete(key);
    }

}