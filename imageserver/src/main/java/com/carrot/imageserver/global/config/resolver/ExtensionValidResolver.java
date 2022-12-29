package com.carrot.imageserver.global.config.resolver;

import com.carrot.imageserver.controller.request.ImageRequest;
import com.carrot.imageserver.service.ImageValidator;
import com.carrot.imageserver.controller.resolver.ExtensionValid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ExtensionValidResolver implements HandlerMethodArgumentResolver {
    private static final String FILE_NAME = "files";
    private final ImageValidator imageValidator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExtensionValid.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        MultipartHttpServletRequest multipartHttpServletRequest = getMultipartHttpServletRequest(webRequest);
        List<MultipartFile> files = multipartHttpServletRequest.getFiles(FILE_NAME);
        files.forEach(this::validateIsExtension);

        return new ImageRequest(files);
    }

    private void validateIsExtension(MultipartFile multipartFile){
        imageValidator.validate(multipartFile);
    }

    private MultipartHttpServletRequest getMultipartHttpServletRequest(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Objects.requireNonNull(httpServletRequest);

        return (MultipartHttpServletRequest) httpServletRequest;
    }
}
