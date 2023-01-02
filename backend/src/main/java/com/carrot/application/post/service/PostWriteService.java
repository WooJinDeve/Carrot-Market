package com.carrot.application.post.service;

import com.carrot.application.post.domain.Post;
import com.carrot.application.post.domain.PostImage;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.region.domain.Region;
import com.carrot.application.region.repository.RegionRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.request.PostRequest.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RegionRepository regionRepository;

    public Long createPost(Long userId, PostSaveRequest request) {
        User user = userRepository.getByIdWithUserRegion(userId);
        userValidator.validateDeleted(user);

        UserRegion userRegion = user.getRepresentUserRegion();
        Region region = regionRepository.getById(userRegion.getRegion().getId());

        Post post = convertToPost(region, request);
        return postRepository.save(post).getId();
    }

    private Post convertToPost(Region region, PostSaveRequest request) {
        Post post = Post.of(region, request.getTitle(), request.getContent(),
                request.getPrice(), request.getCategory(), request.getThumbnail());

        convertToPostImages(request.getImageRequest()).forEach(post::addPostImages);
        return post;
    }

    private List<PostImage> convertToPostImages(List<ImageSaveRequest> requests) {
        return requests.stream()
                .map(image -> PostImage.of(image.getOriginName(), image.getImageUrl()))
                .collect(Collectors.toList());
    }

    public void update(Long userId, Long postId, PostUpdateRequest request) {
        User user = userRepository.getById(userId);
        Post post = postRepository.getById(postId);

        post.verifySoftDeleted();
        post.verifyOwner(user.getId());

        post.update(request.getTitle(), request.getContent(), request.getPrice(),
                request.getCategory(), request.getThumbnail());
    }


    public void delete(Long userId, Long postId) {
        User user = userRepository.getById(userId);
        Post post = postRepository.getById(postId);

        post.verifySoftDeleted();
        post.verifyOwner(user.getId());

        postRepository.delete(post);
    }


}
