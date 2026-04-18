package org.sohel.bookingapp.configuration;

import lombok.RequiredArgsConstructor;
import org.sohel.bookingapp.dto.response.CursorPageResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class L2CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> CursorPageResponse<T> getCursorPageFromL2(String key) {

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        if (!(value instanceof CursorPageResponse<?>)) {
            return null;
        }

        return (CursorPageResponse<T>) value;
    }





    public <T> void put(String key, T value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }
}