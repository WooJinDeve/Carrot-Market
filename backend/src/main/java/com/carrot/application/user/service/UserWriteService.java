package com.carrot.application.user.service;

import com.carrot.application.region.domain.Region;
import com.carrot.application.region.repository.RegionRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
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
    private final RegionRepository regionRepository;
    private final UserRegionRepository userRegionRepository;
    private final UserValidator userValidator;
    private final UserRegionValidator userRegionValidator;


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

        userValidator.validateReRegister(user);
        return user.getId();
    }

    public void saveRegion(Long userId, Long regionId) {
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Region region = regionRepository.getById(regionId);
        userRegionValidator.validateByUserIdAndRegionId(user, region);
        userRegionValidator.validateUserRegionCounter(userId);

        userRegionRepository.save(UserRegion.of(user, region));
    }

    public void deleteRegion(Long userId, Long userRegionId){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        UserRegion userRegion = userRegionRepository.getById(userRegionId);
        userRegionValidator.validateOwner(user, userRegion);
        userRegionRepository.delete(userRegion);
    }
}
