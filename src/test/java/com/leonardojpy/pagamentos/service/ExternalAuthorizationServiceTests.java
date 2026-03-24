package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.client.AuthorizationClient;
import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.entity.WalletType;
import com.leonardojpy.pagamentos.exception.TransactionNotAllowedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExternalAuthorizationServiceTests {

    @Test
    void shouldAcceptAuthorizedResponse() {
        AuthorizationClient client = () -> Map.of(
                "status", "success",
                "data", Map.of("authorization", true)
        );

        var service = new ExternalAuthorizationService(client);

        assertDoesNotThrow(() -> service.authorize(wallet(1L), wallet(2L), BigDecimal.TEN));
    }

    @Test
    void shouldRejectUnauthorizedResponse() {
        AuthorizationClient client = () -> Map.of(
                "status", "fail",
                "data", Map.of("authorization", false)
        );

        var service = new ExternalAuthorizationService(client);

        assertThrows(TransactionNotAllowedException.class, () -> service.authorize(wallet(1L), wallet(2L), BigDecimal.TEN));
    }

    private static Wallet wallet(Long id) {
        var wallet = new Wallet("User " + id, "cpf-" + id, "user" + id + "@mail.com", "123456", WalletType.Enum.USER.get());
        wallet.setId(id);
        wallet.setBalance(new BigDecimal("100.00"));
        return wallet;
    }
}
