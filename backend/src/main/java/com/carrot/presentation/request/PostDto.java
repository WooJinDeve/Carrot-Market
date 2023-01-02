package com.carrot.presentation.request;

import com.carrot.application.post.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class PostDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSaveRequest {
        @NotBlank
        @Length(max = 50)
        private String title;

        @NotBlank
        private String content;

        @Min(value = 0)
        private Integer price;

        @NotBlank
        private Category category;

        @NotBlank
        private String thumbnail;

        @NotBlank
        private List<ImageSaveRequest> imageRequest;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageSaveRequest{
        private String imageUrl;
        private String originName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUpdateRequest {
        @NotBlank
        @Length(max = 50)
        private String title;

        @NotBlank
        private String content;


        @Min(value = 0)
        private Integer price;

        @NotBlank
        private Category category;

        @NotBlank
        private String thumbnail;

    }
}
