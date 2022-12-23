package com.carrot.global.oauth2.principal;

import com.carrot.global.oauth2.provider.ProviderUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


public class PrincipalUser implements OidcUser, OAuth2User {

    private final ProviderUser providerUser;

    public PrincipalUser(ProviderUser providerUser) {
        this.providerUser = providerUser;
    }

    @Override
    public String getName() {
        return providerUser.getEmail();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return providerUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return providerUser.getAuthorities();
    }


    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

}
