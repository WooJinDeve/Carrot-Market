package com.carrot.application.user.service;

import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.oauth2.provider.ProviderUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWriteService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public Long register(ProviderUser providerUser) {
        User user = userRepository.findByEmail(providerUser.getEmail())
                .orElseGet(() -> userRepository.save(
                                User.socialRegister(
                                        providerUser.getEmail(),
                                        providerUser.getUsername(),
                                        providerUser.getPicture(),
                                        providerUser.getProvider(),
                                        providerUser.getId())
                        )
                );

        //만약 회원이 존재하면서 회원탈퇴를 요청한 회원인경우
        userValidator.validateReRegister(user);
        return user.getId();
    }

}
