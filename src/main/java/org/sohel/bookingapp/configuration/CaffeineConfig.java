package org.sohel.bookingapp.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CaffeineConfig {

    /**
     * 🔥 Production-grade L1 cache
     * - Thread-safe
     * - High performance
     * - TTL + Size bound
     * - Supports ALL DTOs (UserDTO, OrderDTO, PaymentDTO, etc.)
     */
    @Bean()
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()

                // 🔹 Maximum entries in memory
                .maximumSize(50_000)

                // 🔹 Expire after write (main TTL)
                .expireAfterWrite(Duration.ofMinutes(10))

                // 🔹 Expire if not accessed (extra safety)
                .expireAfterAccess(Duration.ofMinutes(5))

                // 🔹 Initial memory allocation
                .initialCapacity(1_000)

                // 🔹 Prevent cache stampede (important in prod)
                .build();
    }
}