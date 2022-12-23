package com.carrot.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarrotRuntimeException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public CarrotRuntimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        if (message == null){
            return errorCode.getMessage();
        }
        return String.format("%s, %s", errorCode.getMessage(), message);
    }
}
