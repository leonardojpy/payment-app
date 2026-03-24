package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.client.NotificationClient;
import com.leonardojpy.pagamentos.entity.Transaction;
import com.leonardojpy.pagamentos.exception.ExternalServiceException;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExternalNotificationService implements NotificationService {

    private final NotificationClient notificationClient;

    public ExternalNotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Override
    public void notify(Transaction transaction) {
        try {
            notificationClient.notify(Map.of(
                    "transactionId", transaction.getId(),
                    "payer", transaction.getPayer().getId(),
                    "payee", transaction.getPayee().getId(),
                    "value", transaction.getValue()
            ));
        } catch (FeignException e) {
            throw new ExternalServiceException("Notification service is unavailable");
        }
    }
}
