package com.rate.limiter.ratelimiter.service;
    
 import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final StringRedisTemplate stringRedisTemplate;

    public RateLimitService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean allowRequest(String ipAddress) {
        String key = "ratelimit:" + ipAddress;
        Long currentCount = stringRedisTemplate.opsForValue().increment(key);

        // Set your rate limit and reset interval
        int rateLimit = 10;
        int resetInterval = 60;

        if (currentCount != null && currentCount > rateLimit) {
            // Implement your logic for handling rate limit exceeded
            return false;
        }

        // Set expiration time for the key
        stringRedisTemplate.expire(key, resetInterval, TimeUnit.SECONDS);

        return true;
    }
}