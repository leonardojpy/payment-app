package com.leonardojpy.pagamentos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import com.leonardojpy.pagamentos.entity.WalletType;
import com.leonardojpy.pagamentos.repository.WalletTypeRepository;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final WalletTypeRepository walletTypeRepository;

    public DataLoader(WalletTypeRepository walletTypeRepository) {
        this.walletTypeRepository = walletTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(WalletType.Enum.values())
                .filter(walletType -> !walletTypeRepository.existsByDescription(walletType.getDescription()))
                .forEach(walletType -> walletTypeRepository.save(walletType.get()));
    }
}


