package com.leonardojpy.pagamentos.repository;

import com.leonardojpy.pagamentos.entity.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletTypeRepository extends JpaRepository<WalletType, Long> {
    boolean existsByDescription(String description);

    Optional<WalletType> findFirstByDescriptionOrderByIdAsc(String description);
}
