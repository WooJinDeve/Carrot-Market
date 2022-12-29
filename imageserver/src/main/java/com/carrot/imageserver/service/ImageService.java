package com.carrot.imageserver.service;

import com.carrot.imageserver.controller.response.ImageResponses;
import com.carrot.imageserver.service.dto.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final StoreService storeService;

    public ImageResponses upload(List<MultipartFile> files) {
        List<ImageResponse> images = files.stream()
                .map(storeService::store)
                .collect(Collectors.toList());

        return new ImageResponses(images);
    }
}
