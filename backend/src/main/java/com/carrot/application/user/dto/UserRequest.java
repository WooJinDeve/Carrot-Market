package com.carrot.application.user.dto;

import com.carrot.application.user.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private Long id;
    private Email email;
    private Nickname nickname;
    private MannerTemperature mannerTemperature;
    private String profileUrl;
    private UserRole role;
    private LocalDateTime createdAt;

    public static UserRequest of(User user){
        return UserRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .mannerTemperature(user.getMannerTemperature())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
