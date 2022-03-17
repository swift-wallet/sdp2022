package com.sdp.swiftwallet.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Transaction {
    private final double amount;
    private final Currency curr;

    private final int transactionID; // transactionID to be stored in DB?

    private final String myWallet;
    private final String theirWallet;

    public Transaction(double amount, Currency curr, String myWallet, String theirWallet, int transactionID) {
        this.amount = amount;
        this.curr = curr;
        this.transactionID = transactionID;

        this.myWallet = new String(myWallet);
        this.theirWallet = new String(theirWallet);
    }

    public double getAmount() {
        return amount;
    }

    public double getConvertedAmount() {
        return amount * curr.getValue();
    }

    public String getSymbol() {
        return curr.getSymbol();
    }

    @Override
    public String toString() {
        if (amount < 0) {
            return String.format(
                    Locale.US,
                    "Transaction ID %d\n%f %s from your wallet %s to wallet %s",
                    transactionID, -amount, getSymbol(),
                    myWallet, theirWallet
            );
        } else {
            return String.format(
                    Locale.US,
                    "Transaction ID %d\n%f %s from wallet %s to your wallet %s",
                    transactionID, amount, getSymbol(),
                    theirWallet, myWallet
            );
        }
    }
}
