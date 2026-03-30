package com.snail.repository;

import com.snail.entity.PointTransaction;
import com.snail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUser(User user);
    List<PointTransaction> findByUserOrderByCreatedAtDesc(User user);
    List<PointTransaction> findByType(PointTransaction.TransactionType type);
}