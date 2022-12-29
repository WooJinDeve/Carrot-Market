package com.carrot.imageserver.global.config;

import com.carrot.imageserver.global.config.resolver.ExtensionValidResolver;
import com.carrot.imageserver.service.ImageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final ImageValidator imageValidator;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ExtensionValidResolver(imageValidator));
    }
}
