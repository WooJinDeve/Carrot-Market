package com.carrot.application.post.service;

import com.carrot.application.post.repository.PostImageRepository;
import com.carrot.application.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
}
