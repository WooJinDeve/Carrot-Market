package com.carrot.global.filter;

import com.carrot.application.user.dto.LoginUser;
import com.carrot.application.user.dto.UserRequest;
import com.carrot.application.user.service.UserReadService;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.infrastructure.jwt.service.TokenExtractor;
import com.carrot.infrastructure.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserReadService userReadService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(request);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (CarrotRuntimeException e){
            log.info("[Error] Token validate Exception {}", e.getMessage());
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(HttpServletRequest request) {
        final String token = TokenExtractor.extract(request);
        final Long userId = tokenService.extractUserId(token);

        UserRequest user = userReadService.getUser(userId);
        LoginUser loginUser = LoginUser.of(user);

        return new UsernamePasswordAuthenticationToken(
                loginUser, null,
                List.of(new SimpleGrantedAuthority(loginUser.getRole().toString()))
        );
    }
}
