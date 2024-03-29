package com.carrot.application.post.service;

import com.carrot.application.post.domain.Category;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.PostResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;
    private final UserReadService userReadService;
    public PostDetailResponse findById(final Long postId){
        final Post post = postRepository.getByIdByIdWithRegionAndUserAndImage(postId);
        post.verifySoftDeleted();
        return PostDetailResponse.of(post);
    }

    public PostListResponses findBySearchConditions(final Long userId, final Category category,
                                                    final String title, final Pageable pageable) {
        List<Long> ids = userReadService.findSurroundAreaByUserRegion(userId);
        final Slice<Post> posts = postRepository.findWithSearchConditions(ids, category, title, pageable);
        return PostListResponses.builder()
                .response(convertToPostListResponse(posts.getContent()))
                .hasNext(posts.hasNext())
                .build();
    }

    private List<PostListResponse> convertToPostListResponse(final List<Post> posts) {
        return posts.stream()
                .map(PostListResponse::of)
                .collect(Collectors.toList());
    }

}
