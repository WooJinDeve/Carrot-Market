package com.carrot.testutil.fixture;

import com.carrot.application.post.domain.Category;
import com.carrot.application.post.domain.PostStatue;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;

import java.time.LocalDateTime;
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

    public static Post get(Long id){
        return Post.builder()
                .id(id)
                .build();
    }

    public static Post get(Long id, User user, LocalDateTime deletedAt){
        return Post.builder()
                .id(id)
                .user(user)
                .deletedAt(deletedAt)
                .build();
    }

    public static Post get(User user, Region region, PostSaveRequest request){
        return Post.of(user, region, request.getTitle(), request.getContent(), request.getPrice(),
                request.getCategory(), request.getThumbnail());
    }

    public static Post getStatue(Long id, User user, PostStatue statue){
        return Post.builder()
                .id(id)
                .user(user)
                .statue(statue)
                .build();
    }

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
