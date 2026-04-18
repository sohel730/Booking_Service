package org.sohel.bookingapp.configuration;


import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.sohel.bookingapp.dto.response.CursorPageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class L1CacheService {

    private final Cache<String, Object> cache;

    public <T> T get(String key, Class<T> type) {
        Object value = cache.getIfPresent(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public <T> CursorPageResponse<T> getCursorPage(String key) {
        return (CursorPageResponse<T>) cache.getIfPresent(key);
    }





    public <T> void put(String key, T value) {
        cache.put(key, value);
    }

    public void evict(String key) {
        cache.invalidate(key);
    }
}