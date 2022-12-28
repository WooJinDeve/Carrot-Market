package com.carrot.global.oauth2.provider;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.Nickname;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getId();
    Nickname getUsername();
    Email getEmail();
    String getProvider();
    String getPicture();
    List<? extends GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();
    OAuth2User getOAuth2User();

    //Form 로그인 확장시 필요한 정보
    String getPassword();
}
