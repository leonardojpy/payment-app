package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.controller.dto.CreateWalletDto;
import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.entity.WalletType;
import com.leonardojpy.pagamentos.exception.WalletDataAlreadyExistsException;
import com.leonardojpy.pagamentos.repository.WalletRepository;
import com.leonardojpy.pagamentos.repository.WalletTypeRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletServiceTests {

    @Test
    void shouldRejectWalletCreationWhenCpfCnpjOrEmailAlreadyExists() {
        var walletRepository = proxy(WalletRepository.class, (methodName, args) -> {
            if ("findByCpfCnpjOrEmail".equals(methodName)) {
                return Optional.of(new Wallet());
            }
            return defaultValue(methodName);
        });

        var walletTypeRepository = proxy(WalletTypeRepository.class, (methodName, args) -> Optional.empty());

        var service = new WalletService(walletRepository, walletTypeRepository);
        var dto = new CreateWalletDto("Leonardo", "12345678900", "leo@email.com", "123456", WalletType.Enum.USER, BigDecimal.TEN);

        assertThrows(WalletDataAlreadyExistsException.class, () -> service.createWallet(dto));
    }

    @Test
    void shouldCreateWalletUsingManagedWalletTypeFromDatabase() {
        var managedWalletType = new WalletType(1L, "user");
        var savedWallet = new AtomicReference<Wallet>();

        var walletRepository = proxy(WalletRepository.class, (methodName, args) -> {
            if ("findByCpfCnpjOrEmail".equals(methodName)) {
                return Optional.empty();
            }
            if ("save".equals(methodName)) {
                var wallet = (Wallet) args[0];
                wallet.setId(10L);
                savedWallet.set(wallet);
                return wallet;
            }
            return defaultValue(methodName);
        });

        var walletTypeRepository = proxy(WalletTypeRepository.class, (methodName, args) -> {
            if ("findFirstByDescriptionOrderByIdAsc".equals(methodName)) {
                return Optional.of(managedWalletType);
            }
            return defaultValue(methodName);
        });

        var service = new WalletService(walletRepository, walletTypeRepository);
        var dto = new CreateWalletDto("Leonardo", "12345678900", "leo@email.com", "123456", WalletType.Enum.USER, BigDecimal.TEN);

        var wallet = service.createWallet(dto);

        assertEquals(10L, wallet.getId());
        assertSame(managedWalletType, wallet.getWalletType());
        assertSame(savedWallet.get(), wallet);
        assertEquals(BigDecimal.TEN, wallet.getBalance());
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, ProxyHandler handler) {
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return type.getSimpleName() + "Proxy";
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    return handler.handle(method.getName(), args == null ? new Object[0] : args);
                }
        );
    }

    private static Object defaultValue(String methodName) {
        if ("findAll".equals(methodName)) {
            return java.util.List.of();
        }
        return null;
    }

    @FunctionalInterface
    private interface ProxyHandler {
        Object handle(String methodName, Object[] args);
    }
}
