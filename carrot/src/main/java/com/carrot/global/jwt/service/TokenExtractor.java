package com.carrot.global.jwt.service;

import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.carrot.global.error.ErrorCode.*;

public class TokenExtractor {

    private static final String BEARER_TYPE = "Bearer ";

    public static String extract(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorizationHeader)) {
            throw new CarrotRuntimeException(HEADER_NOTFOUND_ERROR);
        }

        validateAuthorizationFormat(authorizationHeader);
        return authorizationHeader.substring(BEARER_TYPE.length()).trim();
    }

    private static void validateAuthorizationFormat(final String authorizationHeader) {
        if (!authorizationHeader.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new CarrotRuntimeException(TOKEN_VALIDATION_ERROR);
        }
    }
}
