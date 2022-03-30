package com.sdp.swiftwallet.domain.model;

import androidx.annotation.NonNull;

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
     * @param amount        the amount of the transaction
     * @param curr          with which currency the transaction was performed
     * @param myWallet      the ID of this user's wallet
     * @param theirWallet   the ID of the other wallet in the transaction
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
        this.myWallet = myWallet;
        this.theirWallet = theirWallet;
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

    /**
     * Getter for the currency
     *
     * @return the currency of this transaction
     */
    public Currency getCurr() { return curr; }

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

    /**
     * Transaction builder class
     *
     * TODO: add tests for builder
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
