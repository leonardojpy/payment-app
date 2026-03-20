package com.leonardojpy.pagamentos.controller;

import com.leonardojpy.pagamentos.controller.dto.CreateTransactionDto;
import com.leonardojpy.pagamentos.controller.dto.TransactionResponseDto;
import com.leonardojpy.pagamentos.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> listTransactions() {
        var transactions = transactionService.listTransactions().stream()
                .map(TransactionResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody CreateTransactionDto dto) {
        var transaction = transactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponseDto.fromEntity(transaction));
    }
}
