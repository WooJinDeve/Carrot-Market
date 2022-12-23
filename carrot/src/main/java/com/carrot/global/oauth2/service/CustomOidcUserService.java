package com.carrot.global.oauth2.service;

import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.oauth2.principal.PrincipalUser;
import com.carrot.global.oauth2.provider.ProviderUser;
import com.carrot.global.oauth2.provider.ProviderUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    public CustomOidcUserService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = ClientRegistration
                .withClientRegistration(userRequest.getClientRegistration())
                .userNameAttributeName("sub")
                .build();

        OidcUserRequest oidcUserRequest =
                new OidcUserRequest(clientRegistration, userRequest.getAccessToken(),
                        userRequest.getIdToken(), userRequest.getAdditionalParameters());

        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration,oidcUser);
        ProviderUser providerUser = providerUser(providerUserRequest);

        super.register(providerUser);

        return new PrincipalUser(providerUser);
    }
}
