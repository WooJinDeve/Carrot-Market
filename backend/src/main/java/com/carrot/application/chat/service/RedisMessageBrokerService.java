package com.carrot.application.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import static com.carrot.presentation.response.ChatResponse.RecentChatMessageResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageBrokerService {

    private final RedisTemplate<String, RecentChatMessageResponse> redisTemplate;
    private final ChannelTopic channelTopic;

    public void sender(RecentChatMessageResponse response) {
        redisTemplate.convertAndSend(getTopic(), response);
    }

    public String getTopic(){
        return channelTopic.getTopic();
    }
}
