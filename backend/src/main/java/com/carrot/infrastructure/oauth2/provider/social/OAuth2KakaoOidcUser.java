package com.carrot.global.oauth2.provider.social;

import com.carrot.application.user.domain.Nickname;
import com.carrot.global.oauth2.util.Attributes;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2KakaoOidcUser extends OAuth2ProviderUser {

    public OAuth2KakaoOidcUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oAuth2User, clientRegistration);

    }

    @Override
    public String getId() {
        return (String)super.getAttributes().get("id");
    }

    @Override
    public Nickname getUsername() {
        return new Nickname((String) super.getAttributes().get("nickname"));
    }

    @Override
    public String getPicture() {
        return (String)super.getAttributes().get("picture");
    }
}