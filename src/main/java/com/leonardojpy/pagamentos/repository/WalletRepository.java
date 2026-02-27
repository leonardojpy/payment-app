package com.leonardojpy.pagamentos.repository;

import com.leonardojpy.pagamentos.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
