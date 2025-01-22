package com.gfg.walletservice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponse {
    String username;
    Double amount;
}
