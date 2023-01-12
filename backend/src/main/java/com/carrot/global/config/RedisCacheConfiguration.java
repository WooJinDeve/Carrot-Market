package com.carrot.global.config;

import com.carrot.global.config.properties.RedisCacheProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    private final RedisCacheProperties redisCacheProperties;
    private final ObjectMapper objectMapper;

    @Bean(name = "redisCacheConnectionFactory")
    public RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisCacheProperties.getHost());
        configuration.setPort(redisCacheProperties.getPort());
        configuration.setPassword(redisCacheProperties.getPassword());
        return new LettuceConnectionFactory(configuration);
    }


    @Bean
    @Override
    public CacheManager cacheManager() {
        org.springframework.data.redis.cache.RedisCacheConfiguration redisConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                .entryTtl(Duration.ofSeconds(60L));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisCacheConnectionFactory())
                .cacheDefaults(redisConfiguration)
                .build();
    }

    @Bean(name = "redisCacheTemplate")
    public RedisTemplate<String, Object> redisCacheTemplate(@Qualifier(value = "redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return redisTemplate;
    }

    @Bean("stringRedisCacheTemplate")
    public StringRedisTemplate cacheStrRedisTemplate(@Qualifier(value = "redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory) {

        StringRedisTemplate strRedisTemplate = new StringRedisTemplate();
        strRedisTemplate.setConnectionFactory(redisConnectionFactory);
        strRedisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return strRedisTemplate;
    }
}
