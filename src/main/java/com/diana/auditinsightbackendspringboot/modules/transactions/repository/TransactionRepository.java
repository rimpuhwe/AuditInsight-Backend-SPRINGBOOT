package com.diana.auditinsightbackendspringboot.modules.transactions.repository;

import com.diana.auditinsightbackendspringboot.modules.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}