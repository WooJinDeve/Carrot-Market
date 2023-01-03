package com.carrot.testutil.fixture;

import com.carrot.application.post.domain.Category;

import java.util.List;

import static com.carrot.presentation.request.PostRequest.*;

public class PostFixture {

    private static final int BASIC_PRICE = 100;
    private static final String BASIC_THUMBNAIL = "http://BASIC_THUMBNAIL.com";

    private static final String BASIC_IMAGE1 = "http://BASIC_IMAGE1.com";
    private static final String BASIC_ORIGIN1 = "BASIC_IMAGE1";
    private static final String BASIC_IMAGE2 = "http://BASIC_IMAGE2.com";
    private static final String BASIC_ORIGIN2 = "BASIC_IMAGE2";

    private static final List<ImageSaveRequest> BASIC_IMAGES = List.of(
            new ImageSaveRequest(BASIC_IMAGE1, BASIC_ORIGIN1),
            new ImageSaveRequest(BASIC_IMAGE2, BASIC_ORIGIN2)
    );

    public static PostSaveRequest getSaveRequest(String title, String content) {
        return PostSaveRequest.builder()
                .title(title)
                .content(content)
                .price(BASIC_PRICE)
                .thumbnail(BASIC_THUMBNAIL)
                .category(Category.DIGITAL)
                .imageRequest(BASIC_IMAGES)
                .build();
    }

    public static PostUpdateRequest getUpdateRequest(String title, String content) {
        return PostUpdateRequest.builder()
                .title(title)
                .content(content)
                .price(BASIC_PRICE)
                .thumbnail(BASIC_THUMBNAIL)
                .category(Category.DIGITAL)
                .build();
    }
}
