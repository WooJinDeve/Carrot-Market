package com.carrot.application.user.service;

import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.global.error.ErrorCode.*;


@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRegionValidator {

    private final UserRegionRepository userRegionRepository;

    public void validateUserRegionCounter(Long userId) {
        if(userRegionRepository.countByUserId(userId) >= 2){
            throw new CarrotRuntimeException(USER_REGION_MAX_ERROR);
        }
    }

    public void validateOwner(User user, UserRegion userRegion){
        if (!userRegion.isOwner(user.getId())){
            throw new CarrotRuntimeException(USER_REGION_VALIDATION_ERROR);
        }
    }

    public void validateByUserIdAndRegionId(User user, Region region) {
        if (userRegionRepository.existsByUserIdAndRegionId(user.getId(), region.getId()))
            throw new CarrotRuntimeException(USER_REGION_DUPLICATION_ERROR);
    }
}
