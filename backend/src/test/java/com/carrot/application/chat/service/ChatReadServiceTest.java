package com.carrot.application.chat.service;

import com.carrot.application.chat.domain.ChatMessage;
import com.carrot.application.chat.domain.ChatRoom;
import com.carrot.application.chat.repository.ChatMessageRepository;
import com.carrot.application.chat.repository.ChatRoomRepository;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.ChatFixture;
import com.carrot.support.fixture.PostFixture;
import com.carrot.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("[Business] ChatReadServiceTest")
class ChatReadServiceTest extends ServiceTest {

    @InjectMocks
    private ChatReadService chatReadService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @DisplayName("[Success] 채팅방 조회 성공")
    @Test
    void givenUserIdAndPage_whenFinding_thenFindChatRoomList() {
        //given
        User sellerFixture = UserFixture.get(1L);
        User buyerFixture = UserFixture.get(2L);
        Post postFixture = PostFixture.get(1L, sellerFixture, null);
        ChatRoom chatRoomFixture = ChatFixture.get(1L, sellerFixture, buyerFixture, postFixture);
        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<ChatRoom> fixture = new SliceImpl<>(List.of(chatRoomFixture), request, false);

        //when
        when(chatRoomRepository.findAllBySenderIdOrReceiverIdOrderByUpdatedAt(any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> chatReadService.getPageChatRoom(buyerFixture.getId(), request))
                .doesNotThrowAnyException();
    }


    @DisplayName("[Success] 채팅방 조회 성공")
    @Test
    void givenChatRoomIdAndPage_whenFinding_thenFindChatMessageList() {
        //given
        User userFixture = UserFixture.get(1L);
        ChatRoom chatRoomFixture = ChatFixture.get(1L, userFixture, UserFixture.get(2L), null);
        PageRequest request = PageRequest.of(0, 20);
        ChatMessage chatMessageFixture = ChatFixture.get(1L, chatRoomFixture, userFixture, "meesage");
        SliceImpl<ChatMessage> fixture = new SliceImpl<>(List.of(chatMessageFixture), request, false);

        //when
        when(chatMessageRepository.findAllByChatRoomIdWithUserOrderByIdAsc(any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> chatReadService.getByChatRoomIdWithMessage(userFixture.getId(), request))
                .doesNotThrowAnyException();
    }
}