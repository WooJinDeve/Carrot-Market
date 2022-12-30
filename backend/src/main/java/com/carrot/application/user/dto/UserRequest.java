package com.carrot.application.user.dto;

import com.carrot.application.user.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest implements Serializable {

    private Long id;
    private String email;
    private String nickname;
    private double mannerTemperature;
    private String profileUrl;
    private UserRole role;
    private LocalDateTime createdAt;

    public static UserRequest of(User user){
        return UserRequest.builder()
                .id(user.getId())
                .email(user.getEmail().getEmail())
                .nickname(user.getNickname().getNickname())
                .mannerTemperature(user.getMannerTemperature().getMannerTemperature())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
