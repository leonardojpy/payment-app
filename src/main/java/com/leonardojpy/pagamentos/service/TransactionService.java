package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.controller.dto.CreateTransactionDto;
import com.leonardojpy.pagamentos.entity.Transaction;
import com.leonardojpy.pagamentos.entity.WalletType;
import com.leonardojpy.pagamentos.exception.TransactionNotAllowedException;
import com.leonardojpy.pagamentos.exception.WalletNotFoundException;
import com.leonardojpy.pagamentos.repository.TransactionRepository;
import com.leonardojpy.pagamentos.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    public TransactionService(WalletRepository walletRepository,
                              TransactionRepository transactionRepository,
                              AuthorizationService authorizationService,
                              NotificationService notificationService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
    }

    public List<Transaction> listTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction createTransaction(CreateTransactionDto dto) {
        if (dto.payer().equals(dto.payee())) {
            throw new TransactionNotAllowedException("Payer and payee must be different wallets");
        }

        var payer = walletRepository.findById(dto.payer())
                .orElseThrow(() -> new WalletNotFoundException("Payer wallet " + dto.payer() + " was not found"));

        var payee = walletRepository.findById(dto.payee())
                .orElseThrow(() -> new WalletNotFoundException("Payee wallet " + dto.payee() + " was not found"));

        if (payer.getWalletType().getDescription().equals(WalletType.Enum.MERCHANT.getDescription())) {
            throw new TransactionNotAllowedException("Merchant wallets cannot send money");
        }

        if (payer.getBalance().compareTo(dto.value()) < 0) {
            throw new TransactionNotAllowedException("Payer does not have enough balance");
        }

        authorizationService.authorize(payer, payee, dto.value());

        payer.setBalance(payer.getBalance().subtract(dto.value()));
        payee.setBalance(payee.getBalance().add(dto.value()));

        walletRepository.save(payer);
        walletRepository.save(payee);

        var transaction = transactionRepository.save(
                new Transaction(payer, payee, dto.value(), Transaction.Status.AUTHORIZED)
        );

        try {
            notificationService.notify(transaction);
        } catch (RuntimeException e) {
            LOGGER.warn("Falha ao notificar transacao {}", transaction.getId(), e);
        }

        return transaction;
    }
}
