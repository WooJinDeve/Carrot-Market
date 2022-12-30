package com.carrot.infrastructure.oauth2.service;

import com.carrot.infrastructure.oauth2.converter.DelegatingProviderUserConverter;
import com.carrot.infrastructure.oauth2.provider.ProviderUser;
import com.carrot.infrastructure.oauth2.provider.ProviderUserRequest;
import lombok.Getter;

@Getter
public abstract class AbstractOAuth2UserService {

    public ProviderUser providerUser(ProviderUserRequest providerUserRequest){
        return DelegatingProviderUserConverter.convert(providerUserRequest);
    }
}
