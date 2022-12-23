package com.carrot.global.oauth2.converter;

public interface ProviderUserConverter<T, R> {
    boolean support(T t);
    R convert(T t);
}
