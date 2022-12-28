package com.carrot.application.user.domain;


import com.carrot.application.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "users")
@Getter
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

    @Column(name = "certificated_at")
    private LocalDateTime certificatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRegion> userRegions = new HashSet<>();

    @Builder
    public User(Long id, Email email, Nickname nickname, double mannerTemperature, String profileUrl,
                UserRole role, String provider, String providerId, LocalDateTime deletedAt, LocalDateTime certificatedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.mannerTemperature = new MannerTemperature(mannerTemperature);
        this.profileUrl = profileUrl;
        this.role = role;
        this.providerId = providerId;
        this.provider = provider;
        this.deletedAt = deletedAt;
        this.certificatedAt = certificatedAt;
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

    public boolean isDeleted(){
        return Objects.nonNull(deletedAt);
    }

    public void reRegister(){
        this.deletedAt = null;
    }
}
