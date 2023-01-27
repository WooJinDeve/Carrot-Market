package com.carrot.application.post.service;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.domain.entity.TransactionHistory;
import com.carrot.application.post.repository.TransactionHistoryRepository;
import com.carrot.application.user.domain.User;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.PostFixture;
import com.carrot.support.fixture.TransactionHistoryFixture;
import com.carrot.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("[Business] PostReadService")
class TransactionHistoryReadServiceTest extends ServiceTest {

    @InjectMocks
    private TransactionHistoryReadService transactionHistoryReadService;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @DisplayName("[Success] 구매내역 조회 요청")
    @Test
    void givenUserAndPageable_whenFinding_thenPurchasingHistories() {
        //given
        User buyer = UserFixture.get(1L);
        User seller = UserFixture.get(2L);
        Post post = PostFixture.get(1L);

        TransactionHistory historyFixture = TransactionHistoryFixture.get(1L, buyer, seller, post);

        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<TransactionHistory> fixture = new SliceImpl<>(List.of(historyFixture), request, false);

        //when
        when(transactionHistoryRepository.findAllByBuyerIdOrderByIdDesc(any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> transactionHistoryReadService.findByPurchaseHistories(buyer.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 구매내역 조회 요청, 조회된 정보가 존재하지 않을 경우")
    @Test
    void givenUserIdAndPageable_whenFinding_thenNotExistPurchasingHistories() {
        //given
        Long userId = 1L;
        PageRequest request = PageRequest.of(0, 20);

        //when
        when(transactionHistoryRepository.findAllByBuyerIdOrderByIdDesc(any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> transactionHistoryReadService.findByPurchaseHistories(userId, request))
                .doesNotThrowAnyException();
    }


    @DisplayName("[Success] 판매 내역 조회 요청")
    @Test
    void givenUserAndPageable_whenFinding_thenSalesHistories() {
        //given
        User seller = UserFixture.get(1L);
        User buyer = UserFixture.get(2L);
        Post post = PostFixture.get(1L);

        TransactionHistory historyFixture = TransactionHistoryFixture.get(1L, buyer, seller, post);

        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<TransactionHistory> fixture = new SliceImpl<>(List.of(historyFixture), request, false);

        //when
        when(transactionHistoryRepository.findAllBySellerIdOrderByIdDesc(any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> transactionHistoryReadService.findBySalesHistories(seller.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 판매 내역 조회 요청, 조회된 정보가 존재하지 않을 경우")
    @Test
    void givenUserIdAndPageable_whenFinding_thenNotExistSalesHistories() {
        //given
        Long userId = 1L;
        PageRequest request = PageRequest.of(0, 20);

        //when
        when(transactionHistoryRepository.findAllBySellerIdOrderByIdDesc(any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> transactionHistoryReadService.findBySalesHistories(userId, request))
                .doesNotThrowAnyException();
    }
}