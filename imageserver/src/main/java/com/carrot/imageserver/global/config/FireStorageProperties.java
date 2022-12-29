package com.carrot.imageserver.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FireStorageProperties {
    private final String firebaseStorage;
    private final String bucket;

    private final String imageUrl;

    public FireStorageProperties(
            @Value("${app.firebase.configuration-file}")
            String firebaseStorage,
            @Value("${app.firebase.bucket}")
            String bucket,
            @Value("${app.firebase.image-url}")
            String imageUrl
    ) {
        this.firebaseStorage = firebaseStorage;
        this.bucket = bucket;
        this.imageUrl = imageUrl;
    }
}
