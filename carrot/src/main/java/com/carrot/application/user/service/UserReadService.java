package com.carrot.application.user.service;

import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.presentation.response.UserRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.global.error.ErrorCode.USER_REGION_NOTFOUND_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {

    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;

    public UserRegionResponse findRegion(Long userId){
        UserRegion userRegion = userRegionRepository.findByIdAndFetchRegion(userId)
                .orElseThrow(() -> new CarrotRuntimeException(USER_REGION_NOTFOUND_ERROR));
        return UserRegionResponse.of(userRegion);
    }

}
