package com.carrot.global.oauth2.provider.social;

import com.carrot.application.user.domain.Nickname;
import com.carrot.global.oauth2.util.Attributes;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuth2KakaoUser extends OAuth2ProviderUser {

    private final Map<String,Object> subAttributes;

    public OAuth2KakaoUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getSubAttributes(), oAuth2User, clientRegistration);
        this.subAttributes = attributes.getOtherAttributes();

    }

    @Override
    public String getId() {
        return (String)super.getAttributes().get("id");
    }

    @Override
    public Nickname getUsername() {
        return new Nickname((String)this.subAttributes.get("nickname"));
    }

    @Override
    public String getPicture() {
        return (String)this.subAttributes.get("profile_image_url");
    }

}
