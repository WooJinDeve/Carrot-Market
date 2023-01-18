package com.carrot.infrastructure.kafka.consumer;

import com.carrot.application.notification.event.NotificationEvent;
import com.carrot.application.notification.service.NotificationWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationWriteService notificationWriteService;

    @KafkaListener(topics = "${spring.kafka.topic.notification}")
    public void consumeAlarm(NotificationEvent event, Acknowledgment ack){
        log.info("[INFO] consume the event {}", event);
        notificationWriteService.send(event.getReceiverId(), event.getType(), event.getArgs());
        ack.acknowledge();
    }

}
