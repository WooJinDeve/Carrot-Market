package com.carrot.presentation.response;

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
}
