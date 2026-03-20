package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.entity.Wallet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LocalAuthorizationService implements AuthorizationService {

    @Override
    public void authorize(Wallet payer, Wallet payee, BigDecimal value) {
        // Stub local para o teste; na próxima etapa podemos trocar por integração externa.
    }
}
