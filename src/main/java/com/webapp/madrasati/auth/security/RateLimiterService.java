package com.webapp.madrasati.auth.security;


import com.webapp.madrasati.core.error.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {
    private static final int RATE_LIMIT = 100;
    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    public boolean isRequestAllowed(String accessToken) {
        String key = RATE_LIMIT_KEY_PREFIX + accessToken;

        Long currentCount = redisTemplate.opsForValue().increment(key, 1);
        if (currentCount != null) {
            if (currentCount == 1) {
                redisTemplate.expire(key, Duration.ofSeconds(10));
            }
            return currentCount <= RATE_LIMIT;
        }
        throw new InternalServerErrorException("Error rate limiting");
    }
}

