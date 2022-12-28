package com.carrot.application.user.service;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.global.error.ErrorCode.USER_NOTFOUND_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateExistEmail(String email){
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> new CarrotRuntimeException(USER_NOTFOUND_ERROR));
        if (user.isDeleted())
            throw new CarrotRuntimeException(USER_NOTFOUND_ERROR);
    }

    public void validateReRegister(User user){
        if (user.isDeleted()){
            user.reRegister();
        }
    }
}
