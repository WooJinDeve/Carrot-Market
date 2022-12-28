package com.carrot.global.oauth2.service;

import com.carrot.global.oauth2.converter.DelegatingProviderUserConverter;
import com.carrot.global.oauth2.provider.ProviderUser;
import com.carrot.global.oauth2.provider.ProviderUserRequest;
import lombok.Getter;

@Getter
public abstract class AbstractOAuth2UserService {

    public ProviderUser providerUser(ProviderUserRequest providerUserRequest){
        return DelegatingProviderUserConverter.convert(providerUserRequest);
    }
}
