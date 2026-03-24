package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.client.AuthorizationClient;
import com.leonardojpy.pagamentos.entity.Wallet;
import com.leonardojpy.pagamentos.exception.ExternalServiceException;
import com.leonardojpy.pagamentos.exception.TransactionNotAllowedException;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExternalAuthorizationService implements AuthorizationService {

    private final AuthorizationClient authorizationClient;

    public ExternalAuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    @Override
    public void authorize(Wallet payer, Wallet payee, BigDecimal value) {
        try {
            var response = authorizationClient.authorize();

            if (!isAuthorized(response)) {
                throw new TransactionNotAllowedException("Transaction was not authorized by the external service");
            }
        } catch (FeignException e) {
            throw new ExternalServiceException("Authorization service is unavailable");
        }
    }

    private boolean isAuthorized(Map<String, Object> response) {
        if (response == null) {
            return false;
        }

        var data = response.get("data");

        if (data instanceof Map<?, ?> dataMap) {
            var authorization = dataMap.get("authorization");

            if (authorization instanceof Boolean authorized) {
                return authorized;
            }
        }

        var status = response.get("status");
        return status instanceof String stringStatus && "success".equalsIgnoreCase(stringStatus);
    }
}
