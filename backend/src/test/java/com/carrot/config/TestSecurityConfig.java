package com.carrot.config;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.Nickname;
import com.carrot.application.user.dto.UserRequest;
import com.carrot.application.user.service.UserReadService;
import com.carrot.global.config.SecurityConfiguration;
import com.carrot.global.jwt.service.TokenService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.time.LocalDateTime;

import static com.carrot.application.user.domain.UserRole.USER;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfiguration.class)
public class TestSecurityConfig {

    private static final Long USER_ID = 1L;
    @MockBean
    private UserReadService userReadService;
    @MockBean
    private TokenService tokenService;

    @BeforeTestMethod
    public void securitySetup(){
        given(tokenService.extractUserId(anyString()))
                .willReturn(USER_ID);
        given(userReadService.getUser(USER_ID))
                .willReturn(mockUser());
    }

    private UserRequest mockUser(){
        return UserRequest.builder()
                .id(USER_ID)
                .email(new Email("mock@naver.com"))
                .nickname(new Nickname("mocking"))
                .role(USER)
                .profileUrl("http://mocking.com")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
