package com.carrot.testutil.fixture;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.Nickname;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;

import java.time.LocalDateTime;

public class UserFixture {

    private static final String BASIC_EMAIL = "basic@gmail.com";
    private static final String BASIC_NICKNAME = "BASIC_NICKNAME";
    private static final String BASIC_PROFILE_IMAGE = "BASIC_PROFILE_IMAGE";

    public static User get(Long id){
        return User.builder()
                .id(id)
                .email(new Email(BASIC_EMAIL))
                .nickname(new Nickname(BASIC_NICKNAME))
                .profileUrl(BASIC_PROFILE_IMAGE)
                .build();
    }

    public static User get(Long id, LocalDateTime deletedAt){
        return User.builder()
                .id(id)
                .nickname(new Nickname(BASIC_NICKNAME))
                .deletedAt(deletedAt)
                .build();
    }

    public static User getWithUserRegion(Long id, UserRegion userRegionFixture){
        User user = User.builder()
                .id(id)
                .nickname(new Nickname(BASIC_NICKNAME))
                .build();

        user.getUserRegions().add(userRegionFixture);
        return user;
    }
}
