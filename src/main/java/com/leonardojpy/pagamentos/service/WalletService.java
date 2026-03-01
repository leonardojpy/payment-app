package com.leonardojpy.pagamentos.service;
import com.leonardojpy.pagamentos.controller.dto.CreateWalletDto;
import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.exception.WalletDataAlreadyExistsException;
import org.springframework.stereotype.Service;

import com.leonardojpy.pagamentos.repository.WalletRepository;



@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletService) {
        this.walletRepository = walletService;
    }

    public Wallet createWallet(CreateWalletDto dto){

        var walletDb = walletRepository.findByCpfCnpjOrEmail(dto.cpfCnpj(), dto.email());

        if (walletDb.isPresent()){
            throw new WalletDataAlreadyExistsException("CpfCnpj or email already exists");
        }

        return walletRepository.save(dto.toWallet());
    }
}
