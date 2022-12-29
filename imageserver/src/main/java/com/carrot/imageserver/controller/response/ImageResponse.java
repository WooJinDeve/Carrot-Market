package com.carrot.imageserver.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponse {

    private String fileUrl;
    private String originName;

    public ImageResponse(String fileUrl, String originName) {
        this.fileUrl = fileUrl;
        this.originName = originName;
    }
}
