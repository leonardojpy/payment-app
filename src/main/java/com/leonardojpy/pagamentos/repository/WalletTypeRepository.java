package com.leonardojpy.pagamentos.repository;

import com.leonardojpy.pagamentos.entity.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTypeRepository extends JpaRepository<WalletType, Long> {
}
