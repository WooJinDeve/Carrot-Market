package com.carrot.global.oauth2.converter;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.oauth2.provider.ProviderUser;
import com.carrot.global.oauth2.provider.ProviderUserRequest;

import java.util.List;

import static com.carrot.global.error.ErrorCode.OAUTH2_TYPE_VALIDATION_ERROR;

public final class DelegatingProviderUserConverter {
    private static final List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters = List.of(
            new OAuth2KakaoProviderUserConverter(),
            new OAuth2KakaoOidcProviderUserConverter()
    );

    public static ProviderUser convert(ProviderUserRequest request) {
        return converters.stream()
                .filter(provider -> provider.support(request))
                .findAny()
                .orElseThrow(() -> new CarrotRuntimeException(OAUTH2_TYPE_VALIDATION_ERROR))
                .convert(request);
    }
}
