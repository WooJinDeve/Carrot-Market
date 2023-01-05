package com.carrot.application.user.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.region.domain.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Table(name = "user_region")
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserRegion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_region_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Region region;

    private boolean represent;

    @Builder
    public UserRegion(Long id, User user, Region region, boolean represent) {
        this.id = id;
        this.user = user;
        this.region = region;
        this.represent = represent;
    }

    public static UserRegion main(User user, Region region) {
        return UserRegion.builder()
                .user(user)
                .region(region)
                .represent(true)
                .build();
    }

    public static UserRegion sub(User user, Region region){
        return UserRegion.builder()
                .user(user)
                .region(region)
                .represent(false)
                .build();
    }

    public boolean isRepresentative(){
        return this.represent;
    }

    public boolean isOwner(Long userId){
        return Objects.equals(this.user.getId(), userId);
    }
}
