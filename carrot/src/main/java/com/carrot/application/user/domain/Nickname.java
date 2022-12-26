package com.carrot.application.user.domain;

import com.carrot.global.error.CarrotRuntimeException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.carrot.global.error.ErrorCode.NICKNAME_LENGTH_VALIDATION_ERROR;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Nickname {

    private static final int NICKNAME_MAX_LENGTH = 15;

    @Column(name = "nickname", nullable = false, length = 15)
    private String nickname;

    public Nickname(String nickname) {
        this.nickname = nickname;
    }

    public void validateNickname(String nickname){
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new CarrotRuntimeException(NICKNAME_LENGTH_VALIDATION_ERROR);
        }
    }
}
