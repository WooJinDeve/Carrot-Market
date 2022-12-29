package com.carrot.imageserver.global.enums;

import com.carrot.imageserver.global.exception.ImageExtensionException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Extension {
    JPG(1,Arrays.asList("jpg", "JPG")),
    JPEG(2,Arrays.asList("jpeg", "JPEG")),
    PNG(3, Arrays.asList("png", "PNG"));

    private final int idx;
    private final List<String> extensions;

    Extension(int idx, List<String> extensions) {
        this.idx = idx;
        this.extensions = extensions;
    }

    public static void findExtension(String extension){
        Arrays.stream(values())
                .filter(e -> e.hasExtension(extension))
                .findAny()
                .orElseThrow(ImageExtensionException::new);
    }

    public boolean hasExtension(String extension) {
        return this.extensions.stream()
                .anyMatch(ex -> ex.equals(extension));
    }
}
