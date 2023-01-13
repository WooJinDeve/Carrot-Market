package com.carrot.global.config;

import com.carrot.global.config.properties.RedisMessageProperties;
import com.carrot.global.config.socket.RedisSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static com.carrot.presentation.response.ChatResponse.RecentChatMessageResponse;

@Configuration
@RequiredArgsConstructor
public class RedisMessageConfiguration {

    private static final String TOPIC_NAME = "chatroom";
    private final RedisMessageProperties redisMessageProperties;
    private final ObjectMapper objectMapper;


    @Bean(name = "redisMessageConnectionFactory")
    public RedisConnectionFactory redisMessageConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisMessageProperties.getHost());
        configuration.setPort(redisMessageProperties.getPort());
        configuration.setPassword(redisMessageProperties.getPassword());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier(value = "redisMessageConnectionFactory") RedisConnectionFactory redisConnectionFactory,
                                                                       RedisSubscriber redisSubscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisSubscriber, channelTopic());
        return container;
    }

    @Bean(name = "redisMessageTemplate")
    public RedisTemplate<String, RecentChatMessageResponse> redisMessageTemplate(@Qualifier(value = "redisMessageConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, RecentChatMessageResponse> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return redisTemplate;
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(TOPIC_NAME);
    }
}
