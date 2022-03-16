package com.sdp.swiftwallet.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Transaction {
    private double amount;
    private Currency curr;

    private String them; // How to store who the transaction is with?
    private int transactionID; // transactionID to be stored in DB?

    private String myWallet;
    private String theirWallet;

    public Transaction(double amount, Currency curr, String them, int transactionID) {
        this.amount = amount;
        this.curr = curr;
        this.them = them;
        this.transactionID = transactionID;
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
                    "%f %s from your wallet %s to %s\'s wallet %s",
                    -amount, curr.getSymbol(),
                    myWallet, them, theirWallet
            );
        } else {
            return String.format(
                    Locale.US,
                    "%f %s from %s\'s wallet %s to your wallet %s",
                    amount, curr.getSymbol(),
                    them, theirWallet, myWallet
            );
        }
    }

    public static List<Transaction> genDummyHistory() {
        List<Transaction> list = new ArrayList<>();
        Currency curr = new Currency("CoolCoin", "CC", 50);
        list.add(new Transaction(5, curr, "John", 1));
        list.add(new Transaction(4, curr, "Dave", 2));
        list.add(new Transaction(1, curr, "Bob", 3));
        return list;
    }
}
