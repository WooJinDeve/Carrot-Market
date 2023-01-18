package com.carrot.infrastructure.kafka.producer;

import com.carrot.application.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<Long, NotificationEvent> kafkaTemplate;
    @Value("${spring.kafka.topic.notification}")
    private String topic;

    public void send(NotificationEvent event){
        kafkaTemplate.send(topic, event.getReceiverId(), event);
        log.info("[INFO] Send to kafka finished : {}", event);
    }
}
