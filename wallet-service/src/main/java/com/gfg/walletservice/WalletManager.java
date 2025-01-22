package com.gfg.walletservice;

public interface WalletManager {
    void receiveTx(String tx);
    WalletResponse getWalletAmount(String username) throws Exception;
}
