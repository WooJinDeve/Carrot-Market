package com.carrot.testutil.fixture;

import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;

public class UserRegionFixture {


    public static UserRegion get(Long id, User user){
        return UserRegion.builder()
                .id(id)
                .user(user)
                .build();
    }
}
