package com.carrot.application.notification.handler;

import com.carrot.application.article.event.NewCommentNotificationEvent;
import com.carrot.application.article.event.NewReplyNotificationEvent;
import com.carrot.application.like.event.PostLikeNotificationEvent;
import com.carrot.application.notification.domain.NotificationArgs;
import com.carrot.application.notification.event.NotificationEvent;
import com.carrot.infrastructure.kafka.producer.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.carrot.application.notification.domain.NotificationType.NEW_COMMENT_ON_POST;
import static com.carrot.application.notification.domain.NotificationType.NEW_REPLY_ON_COMMENT;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Async
@Component
@RequiredArgsConstructor
@Transactional(propagation = REQUIRES_NEW)
public class NotificationEventHandler {

    private final NotificationProducer notificationProducer;

    @TransactionalEventListener
    public void newCommentHandleNotification(NewCommentNotificationEvent event) {
        NotificationArgs args = new NotificationArgs(event.getSenderId(), event.getCommentId());
        notificationProducer.send(
                new NotificationEvent(
                        event.getReceiverId(),
                        NEW_COMMENT_ON_POST,
                        args)
        );
    }

    @TransactionalEventListener
    public void newReplyHandleNotification(NewReplyNotificationEvent event) {
        NotificationArgs args = new NotificationArgs(event.getSenderId(), event.getReplyId());
        notificationProducer.send(
                new NotificationEvent(
                        event.getReceiverId(),
                        NEW_REPLY_ON_COMMENT,
                        args)
        );
    }

    @TransactionalEventListener
    public void postLikeHandleNotification(PostLikeNotificationEvent event){
        NotificationArgs args = new NotificationArgs(event.getSenderId(), event.getPostId());
        notificationProducer.send(
                new NotificationEvent(
                        event.getTargetId(),
                        NEW_REPLY_ON_COMMENT,
                        args)
        );
    }
}
