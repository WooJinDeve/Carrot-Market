package com.carrot.imageserver.controller;

import com.carrot.imageserver.controller.request.ImageRequest;
import com.carrot.imageserver.controller.response.ImageResponse;
import com.carrot.imageserver.global.common.Response;
import com.carrot.imageserver.service.ImageService;
import com.carrot.imageserver.service.resolver.ExtensionValid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/storage")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public Response<ImageResponse> upload(@ExtensionValid ImageRequest request) {

        return Response.success(new ImageResponse());
    }
}
