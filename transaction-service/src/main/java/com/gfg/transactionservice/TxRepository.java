package com.gfg.transactionservice;

import com.gfg.transactionservice.entity.WalletTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TxRepository extends CrudRepository<WalletTransaction, Long> {
    Optional<WalletTransaction> findByTxId(String txId);
}
