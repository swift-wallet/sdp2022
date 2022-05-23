package com.sdp.swiftwallet.domain.model.transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdp.swiftwallet.domain.model.currency.Currency;

import java.util.Date;
import java.util.Locale;

/**
 * Transaction object
 * Represents a transaction between 2 users and their wallets
 */
public class Transaction {
    private final double amount;
    private final Currency curr;

    private final int transactionID;
    private final Date date;

    private final String senderWalletID;
    private final String receiverWalletID;

    private final String senderID;
    private final String receiverID;

    /**
     * Transaction constructor
     *
     * @param amount the amount of the Transaction, must be greater than 0
     * @param curr the Currency of the Transaction, not null
     * @param date the Date of the Transaction, not null
     * @param transactionID the unique ID of the Transaction
     * @param senderWalletID the ID of the wallet of the sender of this Transaction, not null
     * @param receiverWalletID the ID of the wallet of the receiver of this Transaction, not null
     * @param senderID the ID of the sender of this Transaction if they are a SwiftWallet user
     * @param receiverID the ID of the receiver of this Transaction if they are a SwiftWallet user
     */
    public Transaction(
            double amount,
            @NonNull Currency curr,
            @NonNull Date date,
            int transactionID,
            @NonNull String senderWalletID,
            @NonNull String receiverWalletID,
            @Nullable String senderID,
            @Nullable String receiverID) {
        if (curr == null ||
            date == null ||
            senderWalletID == null ||
            receiverWalletID == null) {
            throw new IllegalArgumentException("Null arguments");
        }

        if (senderWalletID.equals(receiverWalletID)) {
            throw new IllegalArgumentException("Sender and receiver wallets of transaction cannot be the same");
        }

        if (senderID.equals(receiverID)) {
            throw new IllegalArgumentException("Sender and receiver IDs of transaction cannot be the same");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount of transaction must be positive");
        }

        this.amount = amount;
        this.curr = new Currency(curr);

        this.date = new Date(date.getTime());
        this.transactionID = transactionID;

        this.senderWalletID = senderWalletID;
        this.receiverWalletID = receiverWalletID;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    /**
     * TODO
     * @return
     */
    public double getAmount() {
        return amount;
    }

    /**
     * TODO
     * @return
     */
    public Currency getCurr() {
        return new Currency(curr);
    }

    /**
     * TODO
     * @return
     */
    public double getConvertedAmount() {
        return amount * curr.getValue();
    }

    /**
     * TODO
     * @return
     */
    public int getTransactionID() {
        return transactionID;
    }

    /**
     * TODO
     * @return
     */
    public Date getDate() {
        return new Date(date.getTime());
    }

    @Override
    public String toString() {
        /**
         * TODO: do we just limit to users?
         */
        return super.toString();
    }

    /**
     * Transaction builder class
     */
    public static class Builder {
        private double amount;
        private Currency curr;
        private String myWallet, theirWallet;
        private int id;

        /**
         * Default Builder constructor
         * By default, both amount and id are set to 0.
         * setCurr(), setMyWallet() and setTheirWallet() MUST be called before calling build.
         */
        public Builder() {
            amount = 0;
            curr = null;
            myWallet = null;
            theirWallet = null;
            id = 0;
        }

        /**
         * Builder constructor with amount
         * setCurr(), setMyWallet() and setTheirWallet() MUST be called before calling build.
         *
         * @param amount the amount of the transaction
         */
        public Builder(double amount) {
            this();
            this.amount = amount;
        }

        /**
         * Build method.
         * setCurr(), setMyWallet() and setTheirWallet() MUST be called before calling build.
         *
         * @return a new Transaction associated to the parameters of this Builder
         */
        public Transaction build() {
            if (curr == null || myWallet == null || theirWallet == null)
                throw new IllegalStateException("Cannot build transaction with null parameters");
            else return new Transaction(amount, curr, myWallet, theirWallet, id);
        }

        /**
         * Setter for the amount
         *
         * @param amount the amount of the transaction to be built
         * @return the current Builder
         */
        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Setter for the currency
         *
         * @param curr the currency of the transaction to be built
         * @return the current Builder
         */
        public Builder setCurr(Currency curr) {
            this.curr = curr;
            return this;
        }

        /**
         * Setter for the wallet of the user
         *
         * @param myWallet the walletId of the user
         * @return the current Builder
         */
        public Builder setMyWallet(String myWallet) {
            this.myWallet = myWallet;
            return this;
        }

        /**
         * Setter for the wallet of the partner user in the transaction
         *
         * @param theirWallet the walletId of the other user
         * @return the current Builder
         */
        public Builder setTheirWallet(String theirWallet) {
            this.theirWallet = theirWallet;
            return this;
        }

        /**
         * Setter for the id of the current transaction
         *
         * @param id the transactionId of the transaction to be built
         * @return the current Builder
         */
        public Builder setId(int id) {
            this.id = id;
            return this;
        }
    }
}
