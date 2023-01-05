package com.carrot.infrastructure.util;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.error.ErrorCode;

import java.util.Objects;
import java.util.Optional;

public class ClassUtils {

    public static <T>Optional<T> getSafeCastInstance(Object o, Class<T> clazz){
        return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
    }

    public static <T>void checkNotNullParameter(Object o, Class<T> clazz){
        Object parameter = getSafeCastInstance(o, clazz)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR));
        if (Objects.isNull(parameter))
            throw new CarrotRuntimeException(ErrorCode.CLIENT_PARAMETER_ERROR);
    }
}
