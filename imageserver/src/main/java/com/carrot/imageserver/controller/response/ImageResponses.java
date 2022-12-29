package com.carrot.imageserver.controller.response;

import com.carrot.imageserver.service.dto.ImageResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageResponses {

    private List<ImageResponse> responses;

    public ImageResponses(List<ImageResponse> responses) {
        this.responses = responses;
    }
}
