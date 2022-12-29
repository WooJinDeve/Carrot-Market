package com.carrot.imageserver.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageRequest {
    List<MultipartFile> files;

    public ImageRequest(List<MultipartFile> files) {
        this.files = files;
    }
}
