package com.carrot.imageserver.global.exception;

import static com.carrot.imageserver.global.exception.ErrorCode.*;

public class ImageUploadException extends ImageApplicationException{

    public ImageUploadException() {
        super(IMAGE_EXTENSION_VALIDATION_ERROR);
    }
}
