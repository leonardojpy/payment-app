package com.leonardojpy.pagamentos.controller.dto;

import com.leonardojpy.pagamentos.entity.Transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionResponseDto(Long id,
                                     Long payerId,
                                     Long payeeId,
                                     BigDecimal value,
                                     String status,
                                     OffsetDateTime createdAt) {

    public static TransactionResponseDto fromEntity(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getPayer().getId(),
                transaction.getPayee().getId(),
                transaction.getValue(),
                transaction.getStatus().name(),
                transaction.getCreatedAt()
        );
    }
}
