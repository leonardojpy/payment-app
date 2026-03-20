package com.leonardojpy.pagamentos.repository;

import com.leonardojpy.pagamentos.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
