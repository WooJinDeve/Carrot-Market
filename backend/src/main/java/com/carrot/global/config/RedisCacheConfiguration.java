package com.carrot.global.config;

import com.carrot.global.config.properties.RedisCacheProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @Bean(name = "redisCacheConnectionFactory")
    public RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisCacheProperties.getHost());
        configuration.setPort(redisCacheProperties.getPort());
        configuration.setPassword(redisCacheProperties.getPassword());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean("cacheObjectMapper")
    public ObjectMapper cacheObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModules(new JavaTimeModule(), new Jdk8Module());
        return mapper;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        org.springframework.data.redis.cache.RedisCacheConfiguration redisConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(cacheObjectMapper())))
                .entryTtl(Duration.ofSeconds(60L));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisCacheConnectionFactory())
                .cacheDefaults(redisConfiguration)
                .build();
    }

    @Bean(name = "redisCacheTemplate")
    public RedisTemplate<String, Object> redisCacheTemplate(@Qualifier(value = "redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory,
                                                            @Qualifier(value = "cacheObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return redisTemplate;
    }

    @Bean("stringRedisCacheTemplate")
    public StringRedisTemplate cacheStrRedisTemplate(@Qualifier(value = "redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory,
                                                     @Qualifier(value = "cacheObjectMapper") ObjectMapper objectMapper) {

        StringRedisTemplate strRedisTemplate = new StringRedisTemplate();
        strRedisTemplate.setConnectionFactory(redisConnectionFactory);
        strRedisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return strRedisTemplate;
    }
}
