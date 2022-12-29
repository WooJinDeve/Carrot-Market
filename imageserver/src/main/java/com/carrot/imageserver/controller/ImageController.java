package com.carrot.imageserver.controller;

import com.carrot.imageserver.controller.request.ImageRequest;
import com.carrot.imageserver.controller.resolver.ExtensionValid;
import com.carrot.imageserver.controller.response.ImageResponses;
import com.carrot.imageserver.global.common.Response;
import com.carrot.imageserver.service.ImageService;
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
    public Response<ImageResponses> upload(@ExtensionValid ImageRequest request) {
        ImageResponses responses = imageService.upload(request.getFiles());
        return Response.success(responses);
    }
}
