package com.carrot.imageserver.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageRequest {

    private String token;
    List<MultipartFile> files;

    public ImageRequest(String token, List<MultipartFile> files) {
        this.token = token;
        this.files = files;
    }
}
