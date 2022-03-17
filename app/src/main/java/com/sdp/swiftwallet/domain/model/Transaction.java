package com.sdp.swiftwallet.domain.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Transaction object
 * Represents a transaction between 2 wallets
 */
public class Transaction {
    private final double amount;
    private final Currency curr;

    private final int transactionID;

    private final String myWallet;
    private final String theirWallet;

    /**
     * Transaction object constructor
     *
     * @param amount the amount of the transaction
     * @param curr with which currency the transaction was performed
     * @param myWallet the ID of this user's wallet
     * @param theirWallet the ID of the other wallet in the transaction
     * @param transactionID the unique ID of the transaction
     */
    public Transaction(
            double amount,
            @NonNull Currency curr,
            @NonNull String myWallet,
            @NonNull String theirWallet,
            int transactionID) {
        if (curr == null || myWallet == null || theirWallet == null)
            throw new IllegalArgumentException("Null arguments");

        if (myWallet.equals(theirWallet))
            throw new IllegalArgumentException("Wallets cannot be the same");

        this.amount = amount;
        this.curr = curr;
        this.transactionID = transactionID;
        this.myWallet = new String(myWallet);
        this.theirWallet = new String(theirWallet);
    }

    /**
     * Getter for the amount of a transaction
     *
     * @return the amount of this transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Getter for the converted amount of a transaction
     *
     * @return the amount of this transaction, converted according to the currency
     */
    public double getConvertedAmount() {
        return amount * curr.getValue();
    }

    /**
     * Getter for the symbol of this transaction
     *
     * @return the symbol of the currency of this transaction
     */
    public String getSymbol() {
        return curr.getSymbol();
    }

    /**
     * Getter for the transaction ID
     *
     * @return the unique transaction ID for this transaction
     */
    public int getTransactionID() {
        return transactionID;
    }

    @Override
    public String toString() {
        if (amount < 0) {
            return String.format(
                    Locale.US,
                    "%.1f %s from your wallet %s to wallet %s",
                    -amount, getSymbol(),
                    myWallet, theirWallet
            );
        } else {
            return String.format(
                    Locale.US,
                    "%.1f %s from wallet %s to your wallet %s",
                    amount, getSymbol(),
                    theirWallet, myWallet
            );
        }
    }
}
