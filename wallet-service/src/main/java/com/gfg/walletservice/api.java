package com.gfg.walletservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class api {
    @Autowired
    WalletManager walletManager;
    @GetMapping("/api/wallet/{username}")
    ResponseEntity getWallet(@PathVariable("username") String user){
        try {
            WalletResponse walletResponse = walletManager.getWalletAmount(user);
            return ResponseEntity.ok(walletResponse);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
