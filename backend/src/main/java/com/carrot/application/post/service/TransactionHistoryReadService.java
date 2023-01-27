package com.carrot.application.post.service;

import com.carrot.application.post.domain.entity.TransactionHistory;
import com.carrot.application.post.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponse;
import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponses;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionHistoryReadService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryListResponses findByPurchaseHistories(final Long userId, final Pageable pageable) {
        Slice<TransactionHistory> transactionHistories = transactionHistoryRepository.findAllByBuyerIdOrderByIdDesc(userId, pageable);
        return TransactionHistoryListResponses.builder()
                .response(convertToTransactionHistoryListResponse(transactionHistories.getContent()))
                .hasNext(transactionHistories.hasNext())
                .build();
    }

    public TransactionHistoryListResponses findBySalesHistories(final Long userId, final Pageable pageable) {
        Slice<TransactionHistory> transactionHistories = transactionHistoryRepository.findAllBySellerIdOrderByIdDesc(userId, pageable);
        return TransactionHistoryListResponses.builder()
                .response(convertToTransactionHistoryListResponse(transactionHistories.getContent()))
                .hasNext(transactionHistories.hasNext())
                .build();
    }

    private List<TransactionHistoryListResponse> convertToTransactionHistoryListResponse(final List<TransactionHistory> histories) {
        return histories.stream()
                .map(TransactionHistoryListResponse::of)
                .collect(Collectors.toList());
    }
}
