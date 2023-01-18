package com.carrot.application.user.domain;


import com.carrot.application.common.BaseEntity;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.carrot.global.error.ErrorCode.USER_REGION_NOTFOUND_ERROR;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "users")
@Getter
@SQLDelete(sql = "UPDATE USER SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Nickname nickname;

    @Embedded
    private MannerTemperature mannerTemperature;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Column(name = "role", nullable = false)
    @Enumerated(STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRegion> userRegions = new ArrayList<>();

    @Builder
    public User(Long id, Email email, Nickname nickname, Double mannerTemperature, String profileUrl,
                UserRole role, String provider, String providerId, LocalDateTime deletedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.mannerTemperature = Objects.isNull(mannerTemperature) ?
                MannerTemperature.create() : new MannerTemperature(mannerTemperature);
        this.profileUrl = profileUrl;
        this.role = role;
        this.providerId = providerId;
        this.provider = provider;
        this.deletedAt = deletedAt;
    }

    public static User socialRegister(Email email, Nickname nickname, String profileUrl, String provider, String providerId) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .profileUrl(profileUrl)
                .provider(provider)
                .role(UserRole.USER)
                .mannerTemperature(MannerTemperature.create().getMannerTemperature())
                .providerId(providerId)
                .build();
    }

    public UserRegion getRepresentUserRegion(){
        return this.userRegions.stream()
                .filter(UserRegion::isRepresentative)
                .findAny()
                .orElseThrow(() -> new CarrotRuntimeException(USER_REGION_NOTFOUND_ERROR));
    }

    public boolean isDeleted(){
        return Objects.nonNull(deletedAt);
    }

    public void reRegister(){
        this.deletedAt = null;
    }
}
