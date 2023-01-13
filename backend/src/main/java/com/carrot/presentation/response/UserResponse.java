package com.carrot.presentation.response;

import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class UserResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegionResponse implements Serializable {
        private Long id;
        private String name;

        public static UserRegionResponse of(UserRegion userRegion){
            return UserRegionResponse.builder()
                    .id(userRegion.getId())
                    .name(userRegion.getRegion().getName())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileResponse implements Serializable {
        private static final String DELETED_NICKNAME = "unknown";
        private static final String DELETED_PROFILE_URL = "none";

        private Long id;
        private String profileUrl;
        private String nickname;

        public static UserProfileResponse of(User user){
            if (user.isDeleted())
                return softDeleteOf();
            return entityOf(user);
        }


        private static UserProfileResponse entityOf(User user){
            return UserProfileResponse.builder()
                    .id(user.getId())
                    .profileUrl(user.getProfileUrl())
                    .nickname(user.getNickname().getNickname())
                    .build();
        }
        private static UserProfileResponse softDeleteOf(){
            return UserProfileResponse.builder()
                    .nickname(DELETED_NICKNAME)
                    .profileUrl(DELETED_PROFILE_URL)
                    .build();
        }
    }
}
