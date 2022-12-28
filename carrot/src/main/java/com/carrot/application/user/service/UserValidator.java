package com.carrot.application.user.service;

import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.global.error.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;

    public void validateUserRegionCounter(Long userId) {
        if(userRegionRepository.countByUserId(userId) < 2){
            throw new CarrotRuntimeException(USER_REGION_MAX_ERROR);
        }
    }

    public void validateReRegister(User user){
        if (user.isDeleted()){
            user.reRegister();
        }
    }

    public void validateDeleted(User user){
        if (user.isDeleted()){
            throw new CarrotRuntimeException(USER_NOTFOUND_ERROR);
        }
    }

    public void validateOwner(User user, UserRegion userRegion){
        if (!userRegion.isOwner(user.getId())){
            throw new CarrotRuntimeException(USER_REGION_VALIDATION_ERROR);
        }
    }
}
