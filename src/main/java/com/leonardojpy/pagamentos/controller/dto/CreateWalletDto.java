package com.leonardojpy.pagamentos.controller.dto;

import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.entity.WalletType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWalletDto (@NotBlank String fullName,
                               @NotBlank String cpfCnpj,
                               @NotBlank @Email String email,
                               @NotBlank @Size(min = 6, max = 255) String password,
                               @NotNull WalletType.Enum walletType) {

    public Wallet toWallet(WalletType walletType){
        return new Wallet(
                fullName,
                cpfCnpj,
                email,
                password,
                walletType
        );
    }
}
