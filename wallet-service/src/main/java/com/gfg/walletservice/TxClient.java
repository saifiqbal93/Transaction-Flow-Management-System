package com.gfg.walletservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TxClient {
    @GetMapping("/api/tx/{id}")
    TxResponse getTx(@PathVariable("id") String txID) ;
}
