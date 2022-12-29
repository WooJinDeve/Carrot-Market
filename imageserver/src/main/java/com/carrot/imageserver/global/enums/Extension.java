package com.carrot.imageserver.global.enums;

import com.carrot.imageserver.global.exception.ImageExtensionException;

import java.util.Arrays;

public enum Extension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png");
    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }

    public static void findExtension(String extension){
        Arrays.stream(values())
                .filter(e -> e.hasMatch(extension))
                .findFirst()
                .orElseThrow(ImageExtensionException::new);
    }

    public boolean hasMatch(String extension) {
        return this.extension.equals(extension);
    }
}
