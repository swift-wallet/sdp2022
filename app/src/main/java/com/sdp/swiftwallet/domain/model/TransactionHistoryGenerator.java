package com.sdp.swiftwallet.domain.model;

import java.util.List;

public interface TransactionHistoryGenerator {
    public List<Transaction> getTransactionHistory();
}
