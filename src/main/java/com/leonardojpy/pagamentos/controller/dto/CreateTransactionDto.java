package com.leonardojpy.pagamentos.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTransactionDto(@NotNull Long payer,
                                   @NotNull Long payee,
                                   @NotNull @DecimalMin(value = "0.01") BigDecimal value) {
}
