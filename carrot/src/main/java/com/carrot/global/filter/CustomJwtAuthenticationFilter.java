package com.carrot.global.filter;

import com.carrot.application.user.dto.LoginUser;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.jwt.service.TokenExtractor;
import com.carrot.global.jwt.service.TokenService;
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

import static com.carrot.global.error.ErrorCode.USER_NOTFOUND_ERROR;

@Slf4j
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;

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

        LoginUser loginUser = LoginUser.of(userRepository.findById(userId)
                .orElseThrow(() -> new CarrotRuntimeException(USER_NOTFOUND_ERROR)));

        return new UsernamePasswordAuthenticationToken(
                loginUser, null,
                List.of(new SimpleGrantedAuthority(loginUser.getRole().toString()))
        );
    }
}
