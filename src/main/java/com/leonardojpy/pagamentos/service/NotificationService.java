package com.leonardojpy.pagamentos.service;

import com.leonardojpy.pagamentos.entity.Transaction;

public interface NotificationService {
    void notify(Transaction transaction);
}
