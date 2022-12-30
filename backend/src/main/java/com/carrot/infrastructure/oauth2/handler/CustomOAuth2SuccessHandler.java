package com.carrot.global.handler;


import com.carrot.application.user.service.UserWriteService;
import com.carrot.global.jwt.service.TokenCreator;
import com.carrot.global.jwt.token.AuthToken;
import com.carrot.global.oauth2.principal.PrincipalUser;
import com.carrot.global.oauth2.provider.ProviderUser;
import com.carrot.infrastructure.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.carrot.global.handler.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final TokenCreator tokenCreator;
    private final UserWriteService userWriteService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private static final String FRONT_PROD_URL = "http://localhost:3000/oauth2/redirect";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        if (authentication.getPrincipal() instanceof PrincipalUser) {
            ProviderUser providerUser = ((PrincipalUser) authentication.getPrincipal()).getProviderUser();
            Long userId = processOAuth2UserResister(providerUser);
            String targetUrl = determineTargetUrl(request, response, userId);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private Long processOAuth2UserResister(ProviderUser providerUser) {
        return userWriteService.register(providerUser);
    }

    private String determineTargetUrl(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Long userId) {

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUri = redirectUri.orElse(FRONT_PROD_URL);
        AuthToken authToken = tokenCreator.createAuthToken(String.valueOf(userId));

        clearAuthenticationAttributes(request, response);

        return UriComponentsBuilder.fromUriString(targetUri)
                .queryParam("refreshToken", authToken.getRefreshToken())
                .queryParam("accessToken", authToken.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request,
                                                 HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
