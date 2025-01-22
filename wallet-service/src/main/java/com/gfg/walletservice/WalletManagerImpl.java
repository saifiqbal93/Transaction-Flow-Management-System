package com.gfg.walletservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletManagerImpl implements WalletManager{
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TxClient txClient;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Override
    @KafkaListener(topics = "transaction", groupId = "wallet")
    public void receiveTx(String tx) {
        log.info("transaction id received "+tx);
        TxResponse txResponse = txClient.getTx(tx);
        log.info("transaction received for username " +txResponse.getUserID());
        Wallet wallet = walletRepository.findWalletByUsername(txResponse.getUserID())
                .orElse(
                    Wallet.builder().amount(0d).username(txResponse.getUserID()).build()
                );
        switch (txResponse.getTxType()){
            case ADD :
                double currentAmount = wallet.getAmount();
                wallet.setAmount(currentAmount+txResponse.getAmount());
                walletRepository.save(wallet);
                kafkaTemplate.send("wallet", "1"+tx);
                break;
            case SUB:
                currentAmount = wallet.getAmount();
                if(currentAmount - txResponse.getAmount()>0){
                    wallet.setAmount(currentAmount-txResponse.getAmount());
                    kafkaTemplate.send("wallet", "1"+tx);
                }else{
                    kafkaTemplate.send("wallet", "0"+tx);
                }
                walletRepository.save(wallet);
            default:
        }


    }

    @Override
    public WalletResponse getWalletAmount(String username) throws Exception {
        Wallet wallet = walletRepository.findWalletByUsername(username).orElseThrow(
                ()-> new Exception("wallet not found")
        );
        return WalletResponse.builder().amount(wallet.getAmount()).username(wallet.getUsername()).build();
    }
}
