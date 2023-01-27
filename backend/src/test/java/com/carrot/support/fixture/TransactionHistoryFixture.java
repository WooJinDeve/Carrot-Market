package com.carrot.support.fixture;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponse;
import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponses;

public class TransactionHistoryFixture {

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
