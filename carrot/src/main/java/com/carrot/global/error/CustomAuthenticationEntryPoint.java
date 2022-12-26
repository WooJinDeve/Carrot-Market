package com.carrot.global.error;

import com.carrot.global.common.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.carrot.global.error.ErrorCode.TOKEN_VALIDATION_ERROR;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(TOKEN_VALIDATION_ERROR.getStatus().value());
        response.getWriter().write(Response.error(TOKEN_VALIDATION_ERROR.name()).toStream());
    }
}
