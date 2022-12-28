package com.carrot.global.config;

import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CustomAuthenticationEntryPoint;
import com.carrot.global.filter.CustomJwtAuthenticationFilter;
import com.carrot.global.handler.CustomOAuth2FailureHandler;
import com.carrot.global.handler.CustomOAuth2SuccessHandler;
import com.carrot.global.handler.HttpCookieOAuth2AuthorizationRequestRepository;
import com.carrot.global.jwt.service.TokenService;
import com.carrot.global.oauth2.service.CustomOAuth2UserService;
import com.carrot.global.oauth2.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    private final CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    public SecurityConfiguration(CustomOAuth2UserService customOAuth2UserService,
                                 CustomOidcUserService customOidcUserService,
                                 CustomOAuth2SuccessHandler customOAuth2SuccessHandler,
                                 CustomOAuth2FailureHandler customOAuth2FailureHandler,
                                 TokenService tokenService,
                                 UserRepository userRepository) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customOidcUserService = customOidcUserService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.customOAuth2FailureHandler = customOAuth2FailureHandler;
        this.customJwtAuthenticationFilter = new CustomJwtAuthenticationFilter(tokenService, userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("**/swagger", "**/swagger-ui.html", "**/swagger-ui/**").permitAll()
                    .anyRequest().permitAll()
                    .and()
                .formLogin().disable()
                .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository)
                    .and()
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService)
                    .and()
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .and()
                .addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
