package org.sohel.bookingapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

//    json serializer


    @Bean
    public RedisSerializer<Object> jsonSerializer() {
        return RedisSerializer.json();
    }

//    string serializer
    @Bean
    public RedisSerializer<String> stringSerializer() {
        return RedisSerializer.string();
    }


    // MAIN REDIS TEMPLATE

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory,
            RedisSerializer<Object> jsonSerializer,
            RedisSerializer<String> stringSerializer) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // KEY SERIALIZATION
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // VALUE SERIALIZATION (DTOs / Objects)
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }


//  VALUE OPS

    @Bean(name="RedisTemplateForCustomObjectWithValue")
    public ValueOperations<String, Object> valueOps(RedisTemplate<String, Object> template) {
        return template.opsForValue();
    }

//    HASH OPS

    @Bean(name="RedisTemplateForCustomObjectWithHash")
    public HashOperations<String, String, Object> hashOps(RedisTemplate<String, Object> template) {
        return template.opsForHash();
    }

//    LIST OPS

    @Bean(name="RedisTemplateForCustomObjectWithList")
    public ListOperations<String, Object> listOps(RedisTemplate<String, Object> template) {
        return template.opsForList();
    }

//    SET OPS

    @Bean(name="RedisTemplateForCustomObjectWithSet")
    public SetOperations<String, Object> setOps(RedisTemplate<String, Object> template) {
        return template.opsForSet();
    }

//    SORTED SET

    @Bean(name="RedisTemplateForCustomObjectWithZSet")
    public ZSetOperations<String, Object> zSetOps(RedisTemplate<String, Object> template) {
        return template.opsForZSet();
    }


}



