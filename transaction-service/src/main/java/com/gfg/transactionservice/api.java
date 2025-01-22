package com.gfg.transactionservice;


import com.gfg.transactionservice.entity.TxRequest;
import com.gfg.transactionservice.entity.TxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class api {
    @Autowired
    private ITransactionManager manager;

    @PostMapping("/api/tx")
    ResponseEntity<TxResponse> createTx(@RequestBody TxRequest txRequest){
        TxResponse response = manager.CreateTx(txRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tx/{id}")
    ResponseEntity getTx(@PathVariable("id") String id){
        TxResponse response = null;
        try {
            response = manager.GetTransactionStatus(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
