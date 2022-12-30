package com.carrot.infrastructure.oauth2.converter;

import com.carrot.infrastructure.oauth2.provider.ProviderUser;
import com.carrot.infrastructure.oauth2.provider.ProviderUserRequest;
import com.carrot.infrastructure.oauth2.provider.social.OAuth2KakaoOidcUser;
import com.carrot.infrastructure.util.OAuth2Utils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public final class OAuth2KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public boolean support(ProviderUserRequest request) {
        String registrationId = request.getClientRegistration().getRegistrationId();
        if (!SocialType.KAKAO.getSocialName().equals(registrationId)) {
            return false;
        }
        return request.getOAuth2User() instanceof OidcUser;
    }

    @Override
    public ProviderUser convert(ProviderUserRequest request) {
        return new OAuth2KakaoOidcUser(OAuth2Utils.getMainAttributes(
                request.getOAuth2User()),
                request.getOAuth2User(),
                request.getClientRegistration());
    }
}
