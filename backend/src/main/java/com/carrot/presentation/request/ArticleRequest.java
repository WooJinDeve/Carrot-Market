package com.carrot.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class ArticleRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleSaveRequest {

        @NotBlank
        @Length(max = 255)
        private String sentence;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleUpdateRequest {

        @NotBlank
        @Length(max = 255)
        private String sentence;
    }
}
