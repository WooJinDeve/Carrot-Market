package com.carrot.application.user.dto;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

    private Long id;
    private Email email;
    private UserRole role;
    public static LoginUser of(User user){
        return LoginUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
