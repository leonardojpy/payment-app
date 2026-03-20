package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LocalNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalNotificationService.class);

    @Override
    public void notify(Transaction transaction) {
        LOGGER.info("Transferencia {} notificada para carteira recebedora {}", transaction.getId(), transaction.getPayee().getId());
    }
}
