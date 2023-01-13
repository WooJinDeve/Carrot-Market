package com.carrot.application.notification.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.user.domain.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "notification")
@Getter
@TypeDef(name = "json", typeClass = JsonType.class)
@SQLDelete(sql = "UPDATE Notification SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = PROTECTED)
public class Notification extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private NotificationArgs args;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Notification(Long id, User receiver, NotificationType type, NotificationArgs args, LocalDateTime deletedAt) {
        this.id = id;
        this.receiver = receiver;
        this.type = type;
        this.args = args;
        this.deletedAt = deletedAt;
    }

    public static Notification of(User receiver, NotificationType type, NotificationArgs args){
        return Notification.builder()
                .receiver(receiver)
                .type(type)
                .args(args)
                .build();
    }
}
