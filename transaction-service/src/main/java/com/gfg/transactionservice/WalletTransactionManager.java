package com.gfg.transactionservice;

import com.gfg.transactionservice.entity.TxRequest;
import com.gfg.transactionservice.entity.TxResponse;
import com.gfg.transactionservice.entity.TxStatus;
import com.gfg.transactionservice.entity.WalletTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class WalletTransactionManager implements ITransactionManager {
    @Autowired
    TxRepository repository;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public TxResponse CreateTx(TxRequest txRequest){
        String txID = UUID.randomUUID().toString();
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .amount(txRequest.getAmount())
                .date(java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()))
                .txId(txID)
                .txType(txRequest.getTxType())
                .userId(txRequest.getUserID())
                .status(TxStatus.IN_PROCESS)
                .build();
        repository.save(walletTransaction);
        kafkaTemplate.send("transaction", walletTransaction.getTxId());
        return TxResponse.builder().status(walletTransaction.getStatus())
                .amount(walletTransaction.getAmount())
                .userID(walletTransaction.getUserId())
                .txType(walletTransaction.getTxType())
                .txId(walletTransaction.getTxId())
                .build();
    }

    public TxResponse GetTransactionStatus(String txID) throws Exception {
        WalletTransaction walletTransaction = repository.findByTxId(txID)
                .orElseThrow(()-> new Exception("No tx found"));
        return TxResponse.builder().status(walletTransaction.getStatus())
                .amount(walletTransaction.getAmount())
                .userID(walletTransaction.getUserId())
                .txType(walletTransaction.getTxType())
                .txId(walletTransaction.getTxId())
                .build();
    }

    @Override
    @KafkaListener(topics = "wallet", groupId = "transaction")
    public void receive(String walletResponse) {
        String firstChar =  walletResponse.substring(0,1);
        String txID = walletResponse.substring(1,walletResponse.length());
        TxStatus txStatus = TxStatus.IN_PROCESS;

        log.info("wallet response "+walletResponse );
        log.info("status  "+firstChar );
        log.info("txID  "+txID );

        if(firstChar.equals("1")){
            txStatus= TxStatus.SUCCESS;

        }else {
            txStatus = TxStatus.FAILURE;
        }
        Optional<WalletTransaction> transactionOpt = repository.findByTxId(txID);
        if(transactionOpt.isEmpty()){
            log.error("tx not found");
            return;
        }

       WalletTransaction walletTransaction = transactionOpt.get();
        walletTransaction.setStatus(txStatus);
        repository.save(walletTransaction);

        switch (txStatus){
            case SUCCESS :
                kafkaTemplate.send("txnotification","1"+walletTransaction.getUserId());
                break;
            case FAILURE:
                kafkaTemplate.send("txnotification","0"+walletTransaction.getUserId());
            default:
        }
    }
}
