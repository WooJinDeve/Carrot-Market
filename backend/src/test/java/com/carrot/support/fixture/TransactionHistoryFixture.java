package com.carrot.support.fixture;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.domain.entity.TransactionHistory;
import com.carrot.application.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponse;
import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponses;

public class TransactionHistoryFixture {

    public static TransactionHistory get(Long id, User buyer, User seller, Post post){
        return TransactionHistory.builder()
                .id(id)
                .buyer(buyer)
                .seller(seller)
                .post(post)
                .build();
    }

    public static TransactionHistoryListResponses getTransactionHistoryListResponses(long size, boolean hasNext){
        return TransactionHistoryListResponses.builder()
                .response(getConvertToTransactionHistoryListResponse(size))
                .hasNext(hasNext)
                .build();
    }

    private static List<TransactionHistoryListResponse> getConvertToTransactionHistoryListResponse(long size){
        return LongStream.range(1, size + 1)
                .mapToObj(i -> getTransactionHistoryListResponse(i, i))
                .collect(Collectors.toList());
    }

    public static TransactionHistoryListResponse getTransactionHistoryListResponse(Long thId, Long postId){
        return TransactionHistoryListResponse.builder()
                .thId(thId)
                .postId(postId)
                .build();
    }
}
