package com.carrot.application.post.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponses {
    private List<PostListResponse> response;
    private boolean hasNext;

    @Builder
    public PostListResponses(final List<PostListResponse> response,final boolean hasNext) {
        this.response = response;
        this.hasNext = hasNext;
    }
}
