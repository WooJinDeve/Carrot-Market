package com.carrot.application.like.service;

import com.carrot.application.like.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeReadService {

    private final PostLikeRepository postLikeRepository;
}
