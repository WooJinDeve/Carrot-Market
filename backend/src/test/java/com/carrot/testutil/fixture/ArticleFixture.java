package com.carrot.testutil.fixture;

import static com.carrot.presentation.request.ArticleDto.ArticleSaveRequest;
import static com.carrot.presentation.request.ArticleDto.ArticleUpdateRequest;

public class ArticleFixture {


    public static ArticleSaveRequest getSaveRequest(String sentence){
        return ArticleSaveRequest
                .builder()
                .sentence(sentence)
                .build();
    }

    public static ArticleUpdateRequest getUpdateRequest(String sentence){
        return ArticleUpdateRequest
                .builder()
                .sentence(sentence)
                .build();
    }
}
