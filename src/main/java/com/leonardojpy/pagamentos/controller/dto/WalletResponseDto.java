package com.leonardojpy.pagamentos.controller.dto;

import com.leonardojpy.pagamentos.entity.Wallet;

import java.math.BigDecimal;

public record WalletResponseDto(Long id,
                                String fullName,
                                String cpfCnpj,
                                String email,
                                BigDecimal balance,
                                String walletType) {

    public static WalletResponseDto fromEntity(Wallet wallet) {
        return new WalletResponseDto(
                wallet.getId(),
                wallet.getFullName(),
                wallet.getCpfCnpj(),
                wallet.getEmail(),
                wallet.getBalance(),
                wallet.getWalletType().getDescription()
        );
    }
}
