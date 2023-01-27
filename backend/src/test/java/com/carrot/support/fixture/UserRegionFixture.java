package com.carrot.support.fixture;

import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserRegionFixture {

    public static UserRegion get(Long id, User user) {
        return UserRegion.builder()
                .id(id)
                .user(user)
                .represent(true)
                .build();
    }

    public static UserRegion get(Long id, User user, Region region) {
        return UserRegion.builder()
                .id(id)
                .user(user)
                .region(region)
                .represent(true)
                .build();
    }

    public static List<UserRegion> getList(User user, Region region,int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> UserRegion.builder()
                        .user(user)
                        .region(region)
                        .build())
                .collect(Collectors.toList());
    }
}
