package com.carrot.imageserver.service;

import com.carrot.imageserver.global.enums.Extension;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageValidatorIml implements ImageValidator{

    @Override
    public void validate(MultipartFile multipartFile){
        String name = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(name);
        Extension.findExtension(extension);
    }
}
