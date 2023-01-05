package com.carrot.testutil.fixture;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;

import java.time.LocalDateTime;

public class UserFixture {

    private static final String BASIC_EMAIL = "basic@gmail.com";

    public static User get(Long id){
        return User.builder()
                .id(id)
                .email(new Email(BASIC_EMAIL))
                .build();
    }

    public static User get(Long id, LocalDateTime deletedAt){
        return User.builder()
                .id(id)
                .deletedAt(deletedAt)
                .build();
    }

    public static User getWithUserRegion(Long id, UserRegion userRegionFixture){
        User user = User.builder()
                .id(id)
                .build();

        user.getUserRegions().add(userRegionFixture);
        return user;
    }
}
