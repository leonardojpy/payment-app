package com.leonardojpy.pagamentos.controller;

import com.leonardojpy.pagamentos.controller.dto.CreateWalletDto;
import com.leonardojpy.pagamentos.controller.dto.WalletResponseDto;
import com.leonardojpy.pagamentos.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallets")
    public ResponseEntity<List<WalletResponseDto>> listWallets() {
        var wallets = walletService.listWallets().stream()
                .map(WalletResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(wallets);
    }

    @PostMapping("/wallets")
    public ResponseEntity<WalletResponseDto> createWallet(@Valid @RequestBody CreateWalletDto dto){
        var wallet = walletService.createWallet(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(WalletResponseDto.fromEntity(wallet));
    }
}
