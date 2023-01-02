package com.carrot.application.user.domain;


import com.carrot.global.error.CarrotRuntimeException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.carrot.global.error.ErrorCode.EMAIL_VALIDATION_ERROR;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    public Email(String email) {
        verifyEmailPattern(email);
        this.email = email;
    }

    private void verifyEmailPattern(final String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            throw new CarrotRuntimeException(EMAIL_VALIDATION_ERROR);
        }
    }
}
