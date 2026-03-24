package com.leonardojpy.pagamentos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notificationClient", url = "${clients.notification.url}")
public interface NotificationClient {

    @PostMapping("/notify")
    Map<String, Object> notify(@RequestBody Map<String, Object> payload);
}
