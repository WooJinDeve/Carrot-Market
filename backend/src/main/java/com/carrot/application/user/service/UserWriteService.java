package com.carrot.application.user.service;

import com.carrot.application.region.domain.Region;
import com.carrot.application.region.repository.RegionRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.infrastructure.oauth2.provider.ProviderUser;
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
        Long count = userRegionValidator.validateUserRegionCounter(userId);

        UserRegion userRegion = verifyCreateUserRegion(user, region, count);
        userRegionRepository.save(userRegion);
    }

    private UserRegion verifyCreateUserRegion(User user, Region region, Long count){
        if (count == 0)
            return UserRegion.main(user, region);
        return UserRegion.sub(user, region);
    }

    public void deleteRegion(Long userId, Long userRegionId){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        UserRegion userRegion = userRegionRepository.getById(userRegionId);
        userRegionValidator.validateOwner(user, userRegion);

        verifyUserRegionIsRepresentation(userId, userRegion);
        userRegionRepository.delete(userRegion);
    }

    private void verifyUserRegionIsRepresentation(Long userId, UserRegion userRegion){
        if (userRegion.isRepresentative()){
            userRegionRepository.changeNewRepresent(userId);
        }
    }
}
