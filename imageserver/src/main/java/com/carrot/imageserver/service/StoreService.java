package com.carrot.imageserver.service;

import com.carrot.imageserver.service.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

    ImageResponse store(MultipartFile file);
}
