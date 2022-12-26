package com.carrot.global.handler;


import com.carrot.global.jwt.service.TokenCreator;
import com.carrot.global.jwt.token.AuthToken;
import com.carrot.global.oauth2.principal.PrincipalUser;
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
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
  private static final String FRONT_PROD_URL = "http://localhost:3000/oauth2/redirect";

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
          throws ServletException, IOException {
    if (authentication.getPrincipal() instanceof PrincipalUser){      PrincipalUser principal = (PrincipalUser) authentication.getPrincipal();
      String email = principal.getProviderUser().getEmail();
      log.info("[Info] success OAuth Login : {}", principal.getProviderUser().getEmail());

      String targetUrl = determineTargetUrl(request, response, email);

      log.info("[Info] success OAuth Url : {}", targetUrl);
      if (response.isCommitted()) {
        log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        return;
      }
      clearAuthenticationAttributes(request, response);
      getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }else {
      super.onAuthenticationSuccess(request, response, authentication);

    }
  }

  private String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                    String email) {

    Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue);

    String targetUri = redirectUri.orElse(FRONT_PROD_URL);
    AuthToken authToken = tokenCreator.createAuthToken(email);

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
