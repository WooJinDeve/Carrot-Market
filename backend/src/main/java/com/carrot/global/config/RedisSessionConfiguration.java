package com.carrot.global.config;

import com.carrot.global.config.properties.RedisSessionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisHttpSession
public class RedisSessionConfiguration extends AbstractHttpSessionApplicationInitializer {

    private final RedisSessionProperties redisSessionProperties;

    @Primary
    @Bean(name = "redisSessionConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisSessionProperties.getHost());
        configuration.setPort(redisSessionProperties.getPort());
        configuration.setPassword(redisSessionProperties.getPassword());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "redisSessionTemplate")
    public RedisTemplate<String, Object> redisSessionTemplate(@Qualifier("redisSessionConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
