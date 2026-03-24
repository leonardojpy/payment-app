package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.controller.dto.CreateWalletDto;
import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.exception.WalletDataAlreadyExistsException;
import com.leonardojpy.pagamentos.exception.WalletTypeNotFoundException;
import com.leonardojpy.pagamentos.repository.WalletRepository;
import com.leonardojpy.pagamentos.repository.WalletTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTypeRepository walletTypeRepository;

    public WalletService(WalletRepository walletRepository,
                         WalletTypeRepository walletTypeRepository) {
        this.walletRepository = walletRepository;
        this.walletTypeRepository = walletTypeRepository;
    }

    public List<Wallet> listWallets() {
        return walletRepository.findAll();
    }

    public Wallet createWallet(CreateWalletDto dto) {
        var walletDb = walletRepository.findByCpfCnpjOrEmail(dto.cpfCnpj(), dto.email());

        if (walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCnpj or email already exists");
        }

        var walletType = walletTypeRepository.findFirstByDescriptionOrderByIdAsc(dto.walletType().getDescription())
                .orElseThrow(() -> new WalletTypeNotFoundException("Wallet type " + dto.walletType().name() + " does not exist"));

        return walletRepository.save(dto.toWallet(walletType));
    }
}
