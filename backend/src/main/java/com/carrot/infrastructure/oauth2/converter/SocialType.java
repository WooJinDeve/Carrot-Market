package com.carrot.infrastructure.oauth2.converter;

import lombok.Getter;

@Getter
public enum SocialType {
    GOOGLE("google"),
    APPLE("apple"),
    FACEBOOK("facebook"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String socialName;

    SocialType(String socialName) {
        this.socialName = socialName;
    }
}
