package com.carrot.application.user.service;

import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.global.error.ErrorCode.USER_NOTFOUND_ERROR;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

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


}
