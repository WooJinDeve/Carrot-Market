package com.carrot.imageserver.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FireStorageConfiguration {

    private final FireStorageProperties properties;
}
