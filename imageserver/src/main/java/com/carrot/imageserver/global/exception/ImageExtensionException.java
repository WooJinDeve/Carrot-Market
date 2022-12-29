package com.carrot.imageserver.global.exception;


import static com.carrot.imageserver.global.exception.ErrorCode.*;

public class ImageExtensionException extends ImageApplicationException{

    public ImageExtensionException() {
        super(IMAGE_EXTENSION_VALIDATION_ERROR);
    }
}
