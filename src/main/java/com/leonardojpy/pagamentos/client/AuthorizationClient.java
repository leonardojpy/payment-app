package com.leonardojpy.pagamentos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "authorizationClient", url = "${clients.authorization.url}")
public interface AuthorizationClient {

    @GetMapping("/authorize")
    Map<String, Object> authorize();
}
