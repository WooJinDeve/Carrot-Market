package com.carrot.application.post.repository;

import com.carrot.application.post.domain.entity.TransactionHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query(value = "SELECT th FROM TransactionHistory th JOIN FETCH th.post WHERE th.buyer.id = :buyerId")
    Slice<TransactionHistory> findAllByBuyerIdOrderByIdDesc(Long buyerId, Pageable pageable);

    @Query(value = "SELECT th FROM TransactionHistory th JOIN FETCH th.post WHERE th.seller.id = :sellerId")
    Slice<TransactionHistory> findAllBySellerIdOrderByIdDesc(Long sellerId, Pageable pageable);
}
