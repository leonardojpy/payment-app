package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.controller.dto.CreateTransactionDto;
import com.leonardojpy.pagamentos.entity.Transaction;
import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.entity.WalletType;
import com.leonardojpy.pagamentos.exception.TransactionNotAllowedException;
import com.leonardojpy.pagamentos.repository.TransactionRepository;
import com.leonardojpy.pagamentos.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceTests {

    @Test
    void shouldRejectTransferWhenPayerIsMerchant() {
        var merchant = wallet(1L, WalletType.Enum.MERCHANT, "50.00");
        var user = wallet(2L, WalletType.Enum.USER, "10.00");

        var walletRepository = walletRepository(Map.of(1L, merchant, 2L, user));
        var transactionRepository = transactionRepository();

        var service = new TransactionService(
                walletRepository,
                transactionRepository,
                (payer, payee, value) -> {
                },
                transaction -> {
                }
        );

        var dto = new CreateTransactionDto(1L, 2L, new BigDecimal("10.00"));

        assertThrows(TransactionNotAllowedException.class, () -> service.createTransaction(dto));
    }

    @Test
    void shouldRejectTransferWhenPayerHasNotEnoughBalance() {
        var payer = wallet(1L, WalletType.Enum.USER, "5.00");
        var payee = wallet(2L, WalletType.Enum.USER, "10.00");

        var walletRepository = walletRepository(Map.of(1L, payer, 2L, payee));
        var transactionRepository = transactionRepository();

        var service = new TransactionService(
                walletRepository,
                transactionRepository,
                (from, to, value) -> {
                },
                transaction -> {
                }
        );

        var dto = new CreateTransactionDto(1L, 2L, new BigDecimal("10.00"));

        assertThrows(TransactionNotAllowedException.class, () -> service.createTransaction(dto));
    }

    @Test
    void shouldTransferBalanceAndPersistTransaction() {
        var payer = wallet(1L, WalletType.Enum.USER, "100.00");
        var payee = wallet(2L, WalletType.Enum.MERCHANT, "25.00");
        var savedTransaction = new AtomicReference<Transaction>();
        var notificationTransaction = new AtomicReference<Transaction>();

        var walletRepository = walletRepository(Map.of(1L, payer, 2L, payee));
        var transactionRepository = transactionRepository(savedTransaction);

        var service = new TransactionService(
                walletRepository,
                transactionRepository,
                (from, to, value) -> assertEquals(new BigDecimal("10.00"), value),
                notificationTransaction::set
        );

        var dto = new CreateTransactionDto(1L, 2L, new BigDecimal("10.00"));

        var transaction = service.createTransaction(dto);

        assertEquals(new BigDecimal("90.00"), payer.getBalance());
        assertEquals(new BigDecimal("35.00"), payee.getBalance());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertNotNull(transaction.getCreatedAt());
        assertEquals(savedTransaction.get(), notificationTransaction.get());
    }

    @SuppressWarnings("unchecked")
    private static WalletRepository walletRepository(Map<Long, Wallet> wallets) {
        return (WalletRepository) Proxy.newProxyInstance(
                WalletRepository.class.getClassLoader(),
                new Class[]{WalletRepository.class},
                (proxy, method, args) -> {
                    if ("findById".equals(method.getName())) {
                        return Optional.ofNullable(wallets.get((Long) args[0]));
                    }
                    if ("save".equals(method.getName())) {
                        return args[0];
                    }
                    if ("findAll".equals(method.getName())) {
                        return wallets.values().stream().toList();
                    }
                    if ("toString".equals(method.getName())) {
                        return "WalletRepositoryProxy";
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    return null;
                }
        );
    }

    @SuppressWarnings("unchecked")
    private static TransactionRepository transactionRepository() {
        return transactionRepository(new AtomicReference<>());
    }

    @SuppressWarnings("unchecked")
    private static TransactionRepository transactionRepository(AtomicReference<Transaction> savedTransaction) {
        return (TransactionRepository) Proxy.newProxyInstance(
                TransactionRepository.class.getClassLoader(),
                new Class[]{TransactionRepository.class},
                (proxy, method, args) -> {
                    if ("save".equals(method.getName())) {
                        var transaction = (Transaction) args[0];
                        transaction.setId(99L);
                        savedTransaction.set(transaction);
                        return transaction;
                    }
                    if ("findAll".equals(method.getName())) {
                        return java.util.List.of();
                    }
                    if ("toString".equals(method.getName())) {
                        return "TransactionRepositoryProxy";
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    return null;
                }
        );
    }

    private static Wallet wallet(Long id, WalletType.Enum walletTypeEnum, String balance) {
        var walletType = walletTypeEnum.get();
        var wallet = new Wallet("User " + id, "cpf-" + id, "user" + id + "@mail.com", "123456", walletType);
        wallet.setId(id);
        wallet.setBalance(new BigDecimal(balance));
        return wallet;
    }
}
