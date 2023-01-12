package com.carrot.application.chat.service;

import com.carrot.application.chat.domain.ChatMessage;
import com.carrot.application.chat.domain.ChatRoom;
import com.carrot.application.chat.repository.ChatMessageRepository;
import com.carrot.application.chat.repository.ChatRoomRepository;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatWriteService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public void create(Long buyerId, Long postId){
        User buyer = userRepository.getById(buyerId);
        Post post = postRepository.getByIdWithUser(postId);
        User seller = post.getUser();

        post.verifySoftDeleted();
        userValidator.validateDeleted(buyer);
        userValidator.validateDeleted(seller);

        //TODO : 이미 판매된 상품에는 채팅을 할 수 없게 한다.

        chatRoomRepository.save(ChatRoom.of(seller, buyer, post));
    }

    public void send(Long chatRoomId, Long userId, String message){
        ChatRoom chatRoom = chatRoomRepository.getByIdWithPost(chatRoomId);
        User sender = userRepository.getById(userId);
        userValidator.validateDeleted(sender);
        chatRoom.verifySoftDeleted();
        chatRoom.verifyOrganizer(userId);

        chatMessageRepository.save(ChatMessage.of(chatRoom, sender, message));
    }
}
