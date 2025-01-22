package com.gfg.walletservice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TxResponse {
    private String userID;
    private double amount;
    private TxStatus status;
    private TxType txType;
    private  String txId;
}
