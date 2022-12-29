package com.carrot.application.user.dto;

import com.carrot.application.user.domain.Email;
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
    public static LoginUser of(UserRequest request){
        return LoginUser.builder()
                .id(request.getId())
                .email(request.getEmail())
                .role(request.getRole())
                .build();
    }

}
