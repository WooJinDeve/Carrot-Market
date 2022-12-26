package com.carrot.global.oauth2.service;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.oauth2.converter.DelegatingProviderUserConverter;
import com.carrot.global.oauth2.provider.ProviderUser;
import com.carrot.global.oauth2.provider.ProviderUserRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {
    private final UserRepository userRepository;

    public void register(ProviderUser providerUser) {
        User user = userRepository.findByEmail(new Email(providerUser.getEmail()))
                .orElseGet(() -> User.socialRegister(
                        providerUser.getEmail(),
                        providerUser.getUsername(),
                        providerUser.getPicture(),
                        providerUser.getProvider(),
                        providerUser.getId()));
        userRepository.save(user);
    }

    public ProviderUser providerUser(ProviderUserRequest providerUserRequest){
        return DelegatingProviderUserConverter.convert(providerUserRequest);
    }
}
