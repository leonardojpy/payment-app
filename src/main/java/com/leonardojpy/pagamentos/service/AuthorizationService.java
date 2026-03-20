package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.entity.Wallet;

import java.math.BigDecimal;

public interface AuthorizationService {
    void authorize(Wallet payer, Wallet payee, BigDecimal value);
}
