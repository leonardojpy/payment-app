package com.leonardojpy.pagamentos.controller.dto;

import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.entity.WalletType;

public record CreateWalletDto (String fullName,
                               String cpfCnpj,
                               String email,
                               String password,
                               WalletType.Enum walletType) {

    public Wallet toWallet(){
        return new Wallet(
                fullName,
                cpfCnpj,
                email,
                password,
                walletType.get()
        );
    }
}
