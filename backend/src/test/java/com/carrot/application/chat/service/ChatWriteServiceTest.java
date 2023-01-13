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
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.testutil.ServiceTest;
import com.carrot.testutil.fixture.ChatFixture;
import com.carrot.testutil.fixture.PostFixture;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static com.carrot.global.error.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("[Business] ChatWriteService")
class ChatWriteServiceTest extends ServiceTest {

    @InjectMocks
    private ChatWriteService chatWriteService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private RedisMessageBrokerService redisMessageBrokerService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserValidator userValidator;

    @DisplayName("[Success] 채팅방 생성 요청")
    @Test
    void givenBuyerIdAndPostId_whenSaving_thenSaveChatRoom() {
        //given
        User buyerFixture = UserFixture.get(1L);
        User sellerFixture = UserFixture.get(2L);
        Post postFixture = PostFixture.get(1L, sellerFixture, null);

        //when
        when(userRepository.getById(any())).thenReturn(buyerFixture);
        when(postRepository.getByIdWithUser(any())).thenReturn(postFixture);
        when(chatRoomRepository.save(any())).thenReturn(mock(ChatRoom.class));

        //then
        assertThatCode(() -> chatWriteService.create(buyerFixture.getId(), postFixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 채팅방 생성 요청시, 삭제된 게시물인 경우")
    @Test
    void givenBuyerIdAndPostId_whenSaving_thenThrowSoftDeletedPost() {
        //given
        User buyerFixture = UserFixture.get(1L);
        User sellerFixture = UserFixture.get(2L);
        Post postFixture = PostFixture.get(1L, sellerFixture, now());

        //when
        when(userRepository.getById(any())).thenReturn(buyerFixture);
        when(postRepository.getByIdWithUser(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.create(buyerFixture.getId(), postFixture.getId()));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 채팅방 생성 요청시, 탈퇴한 유저가 존재할 경우")
    @Test
    void givenBuyerIdAndPostId_whenSaving_thenThrowSoftDeletedUser() {
        //given
        User deletedUSer = UserFixture.get(1L,now());
        User sellerFixture = UserFixture.get(2L);
        Post postFixture = PostFixture.get(1L, sellerFixture, null);

        //when
        when(userRepository.getById(any())).thenReturn(deletedUSer);
        when(postRepository.getByIdWithUser(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.create(deletedUSer.getId(), postFixture.getId()));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }


    @DisplayName("[Success] 채팅방 삭제 요청")
    @Test
    void givenUserIdAndChatRoomId_whenDeleting_thenDeletedChatRoom() {
        //given
        User userFixture = UserFixture.get(1L);
        ChatRoom chatRoomFixture = ChatFixture.get(1L, userFixture, UserFixture.get(2L), null);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(chatRoomRepository.getById(any())).thenReturn(chatRoomFixture);
        doNothing().when(chatRoomRepository).delete(any());

        //then
        assertThatCode(() -> chatWriteService.delete(userFixture.getId(), chatRoomFixture.getId()))
                .doesNotThrowAnyException();
    }


    @DisplayName("[Error] 채팅방 삭제 요청시, 탈퇴한 유저일 경우")
    @Test
    void givenUserIdAndChatRoomId_whenDeleting_thenThrowSoftDeletedUser() {
        //given
        User userFixture = UserFixture.get(1L,now());

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.delete(userFixture.getId(), any()));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 채팅방 삭제 요청시, 채팅방이 존재하지 않을 경우")
    @Test
    void givenUserIdAndChatRoomId_whenDeleting_thenThrowNotExistChatRoom() {
        //given
        User userFixture = UserFixture.get(1L);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        doThrow(new CarrotRuntimeException(CHATROOM_NOTFOUND_ERROR)).when(chatRoomRepository).getById(any());

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.delete(userFixture.getId(), any()));
        assertThat(CHATROOM_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 채팅방 삭제 요청시, 채팅방에 참여하지 않은 사용자일 경우")
    @Test
    void givenUserIdAndChatRoomId_whenDeleting_thenThrowNotOrganizer() {
        //given
        User notOrganizerFixture = UserFixture.get(3L);
        ChatRoom chatRoomFixture = ChatFixture.get(1L, UserFixture.get(1L), UserFixture.get(2L), null);

        //when
        when(userRepository.getById(any())).thenReturn(notOrganizerFixture);
        when(chatRoomRepository.getById(any())).thenReturn(chatRoomFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.delete(notOrganizerFixture.getId(), chatRoomFixture.getId()));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 채팅 메시지 전송 요청")
    @Test
    void givenChatRoomIdAndUserIdAndMessage_whenSend_thenSendMessage() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);
        ChatRoom chatRoomFixture = ChatFixture.get(1L, userFixture, UserFixture.get(2L), postFixture);
        ChatMessage chatFixture = ChatFixture.get(1L, chatRoomFixture, userFixture, "message");

        //when
        when(chatRoomRepository.getByIdWithPost(any())).thenReturn(chatRoomFixture);
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(chatMessageRepository.save(any())).thenReturn(chatFixture);
        doNothing().when(redisMessageBrokerService).sender(any());

        //then
        assertThatCode(() -> chatWriteService.send(chatRoomFixture.getId(), userFixture.getId(), any()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 채팅 메시지 전송 요청시, 채팅방이 존재하지 않은 경우")
    @Test
    void givenChatRoomIdAndUserIdAndMessage_whenSend_thenThrowNotExistChatRoom() {
        //given
        Long chatRoomId = 1L;
        Long userId= 1L;

        //when
        doThrow(new CarrotRuntimeException(CHATROOM_NOTFOUND_ERROR)).when(chatRoomRepository).getByIdWithPost(any());

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.send(chatRoomId, userId, any()));
        assertThat(CHATROOM_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 채팅 메시지 전송 요청, 게시물이 삭제된 경우")
    @Test
    void givenChatRoomIdAndUserIdAndMessage_whenThrowNotExistPost() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, now());
        ChatRoom chatRoomFixture = ChatFixture.get(1L, userFixture, UserFixture.get(2L), postFixture);

        //when
        when(chatRoomRepository.getByIdWithPost(any())).thenReturn(chatRoomFixture);
        when(userRepository.getById(any())).thenReturn(userFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.send(chatRoomFixture.getId(), userFixture.getId(), any()));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 채팅 메시지 전송 요청, 게시물이 삭제된 경우")
    @Test
    void givenChatRoomIdAndUserIdAndMessage_whenThrowSoftDeletedChatRoom() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);
        ChatRoom chatRoomFixture = ChatFixture.getDeleted(1L, userFixture, UserFixture.get(2L), postFixture);

        //when
        when(chatRoomRepository.getByIdWithPost(any())).thenReturn(chatRoomFixture);
        when(userRepository.getById(any())).thenReturn(userFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.send(chatRoomFixture.getId(), userFixture.getId(), any()));
        assertThat(CHATROOM_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }


    @DisplayName("[Error] 채팅 메시지 전송 요청시, 채팅방에 참여하지 않은 사용자일 경우")
    @Test
    void givenChatRoomIdAndUserIdAndMessage_whenSend_thenThrowNotOrganizer() {
        //given
        User notOrganizerFixture = UserFixture.get(3L);
        Post postFixture = PostFixture.get(1L, UserFixture.get(1L), null);
        ChatRoom chatRoomFixture = ChatFixture.get(1L, UserFixture.get(1L), UserFixture.get(2L), postFixture);

        //when
        when(chatRoomRepository.getByIdWithPost(any())).thenReturn(chatRoomFixture);
        when(userRepository.getById(any())).thenReturn(notOrganizerFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> chatWriteService.send(chatRoomFixture.getId(), notOrganizerFixture.getId(), any()));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }
}
