package com.carrot.testutil.fixture;

import com.carrot.application.user.domain.User;

import java.time.LocalDateTime;

public class UserFixture {

    public static User get(Long id){
        return User.builder()
                .id(id)
                .build();
    }

    public static User get(Long id, LocalDateTime deletedAt){
        return User.builder()
                .id(id)
                .deletedAt(deletedAt)
                .build();
    }
}
