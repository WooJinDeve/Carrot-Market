package com.carrot.imageserver.service;

import com.carrot.imageserver.global.config.FireStorageProperties;
import com.carrot.imageserver.global.exception.ImageUploadException;
import com.carrot.imageserver.service.dto.ImageResponse;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final FireStorageProperties properties;
    @Override
    public ImageResponse store(MultipartFile file) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            String name = generateUniqueFileName();
            bucket.create(name, file.getBytes(), file.getContentType());
            return new ImageResponse(getImageUrl(name), file.getOriginalFilename());
        }catch (IOException e){
            throw new ImageUploadException();
        }
    }

    public String getImageUrl(String name) {
        return String.format(properties.getImageUrl(), name);
    }

    private String generateUniqueFileName(){
        return UUID.randomUUID().toString();
    }
}
