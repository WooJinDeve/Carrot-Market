package com.carrot.global.config;

import com.carrot.global.oauth2.service.CustomOAuth2UserService;
import com.carrot.global.oauth2.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                                userInfoEndpointConfig -> userInfoEndpointConfig
                                        .userService(customOAuth2UserService)
                                        .oidcUserService(customOidcUserService)));

        return http.build();
    }
}
