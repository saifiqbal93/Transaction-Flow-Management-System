package com.gfg.transactionservice;

import com.gfg.transactionservice.entity.TxRequest;
import com.gfg.transactionservice.entity.TxResponse;


public interface ITransactionManager {
    TxResponse CreateTx(TxRequest txRequest);
    TxResponse GetTransactionStatus(String txID) throws Exception;

    void receive(String walletResponse);
}
