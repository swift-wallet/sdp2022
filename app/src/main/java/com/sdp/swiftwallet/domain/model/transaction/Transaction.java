package com.sdp.swiftwallet.domain.model.transaction;

import androidx.annotation.NonNull;

import com.sdp.swiftwallet.domain.model.currency.Currency;

import java.util.Date;
import java.util.Optional;

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

    private final Optional<String> senderID;
    private final Optional<String> receiverID;

    /**
     * Transaction constructor
     *
     * @param amount           the amount of the Transaction, must be greater than 0
     * @param curr             the Currency of the Transaction, not null
     * @param transactionID    the unique ID of the Transaction
     * @param date             the Date of the Transaction, not null
     * @param senderWalletID   the ID of the wallet of the sender of this Transaction, not null
     * @param receiverWalletID the ID of the wallet of the receiver of this Transaction, not null
     * @param senderID         the ID of the sender of this Transaction if they are a SwiftWallet user
     * @param receiverID       the ID of the receiver of this Transaction if they are a SwiftWallet user
     */
    public Transaction(
            double amount,
            @NonNull Currency curr,
            int transactionID,
            @NonNull Date date,
            @NonNull String senderWalletID,
            @NonNull String receiverWalletID,
            @NonNull Optional<String> senderID,
            @NonNull Optional<String> receiverID) {
        if (curr == null ||
                date == null ||
                senderWalletID == null ||
                receiverWalletID == null) {
            throw new IllegalArgumentException("Null arguments");
        }

        if (senderWalletID.equals(receiverWalletID)) {
            throw new IllegalArgumentException("Sender and receiver wallets of transaction cannot be the same");
        }

        if (senderID.isPresent() && receiverID.isPresent()) {
            if (senderID.get().equals(receiverID.get())) {
                throw new IllegalArgumentException("Sender and receiver IDs of transaction cannot be the same");
            }
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
     * Getter for the amount of this transaction
     *
     * @return the amount of this transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Getter for the Currency of this transaction
     *
     * @return the Currency of this transaction
     */
    public Currency getCurr() {
        return new Currency(curr);
    }

    /**
     * Getter for the amount in USD of this transaction
     *
     * @return the amount of this transaction, in USD
     */
    public double getConvertedAmount() {
        return amount * curr.getValue();
    }

    /**
     * Getter for the unique ID of this transaction
     *
     * @return the unique ID of this transaction
     */
    public int getTransactionID() {
        return transactionID;
    }

    /**
     * Getter for the Date of this transaction
     *
     * @return the Date of this transaction
     */
    public Date getDate() {
        return new Date(date.getTime());
    }

    /**
     * Getter for the ID of this transaction's sender's wallet
     *
     * @return the ID of the wallet of this transaction's sender
     */
    public String getSenderWalletID() {
        return senderWalletID;
    }

    /**
     * Getter for the ID of this transaction's receiver's wallet
     *
     * @return the ID of the wallet of this transaction's receiver
     */
    public String getReceiverWalletID() {
        return receiverWalletID;
    }

    /**
     * Getter for the ID of this transaction's sender, if they are a SwiftWallet user
     *
     * @return the ID of this transaction's sender
     */
    public Optional<String> getSenderID() {
        return senderID;
    }

    /**
     * Getter for the ID of this transaction's receiver, if they are a SwiftWallet user
     *
     * @return the ID of this transaction's receiver
     */
    public Optional<String> getReceiverID() {
        return receiverID;
    }

    /**
     * Transaction builder class
     */
    public static class Builder {
        private double amount;
        private Currency curr;

        private int transactionID;
        private Date date;

        private String senderWalletID;
        private String receiverWalletID;

        private Optional<String> senderID;
        private Optional<String> receiverID;

        /**
         * Build method
         *
         * @return a Transaction with the parameters of this Builder
         */
        public Transaction build() {
            return new Transaction(
                    amount,
                    curr,
                    transactionID,
                    date,
                    senderWalletID,
                    receiverWalletID,
                    senderID,
                    receiverID
            );
        }

        /**
         * Default Builder constructor
         * Amount and transactionID are set to 0
         * senderID and receiverID are empty Optionals
         * The other parameters are set to null (will trigger an error when building if not set
         * before calling build())
         */
        public Builder() {
            this.amount = 0;
            this.curr = null;
            this.transactionID = 0;
            this.date = null;
            this.senderWalletID = null;
            this.receiverWalletID = null;
            this.senderID = Optional.empty();
            this.receiverID = Optional.empty();
        }

        /**
         * Setter for the amount of the Transaction to be built
         *
         * @param amount the desired amount of the Transaction
         * @return this Builder
         */
        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Setter for the Currency of the Transaction to be built
         *
         * @param curr the desired Currency of the Transaction
         * @return this Builder
         */
        public Builder setCurrency(Currency curr) {
            this.curr = curr;
            return this;
        }

        /**
         * Combo setter for the amount and Currency of the Transaction to be built
         *
         * @param amount the desired amount of the Transaction
         * @param curr   the desired Currency of the Transaction
         * @return this Builder
         */
        public Builder setAmountAndCurrency(double amount, Currency curr) {
            return this.setAmount(amount).setCurrency(curr);
        }

        /**
         * Setter for the unique transaction ID of the Transaction to be built
         *
         * @param transactionID the unique ID of the Transaction
         * @return this Builder
         */
        public Builder setTransactionID(int transactionID) {
            this.transactionID = transactionID;
            return this;
        }

        /**
         * Setter for the Date of the Transaction to be built
         *
         * @param date the desired Date of the Transaction
         * @return this Builder
         */
        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        /**
         * Setter for the metadata of the Transaction to be built
         *
         * @param transactionID the unique ID of the Transaction
         * @param date          the desired Date of the Transaction
         * @return this Builder
         */
        public Builder setMetadata(int transactionID, Date date) {
            return this.setTransactionID(transactionID).setDate(date);
        }

        /**
         * Setter for the SenderWalletID of the Transaction to be built
         *
         * @param senderWalletID the ID of the wallet of the sender of the Transaction
         * @return this Builder
         */
        public Builder setSenderWalletID(String senderWalletID) {
            this.senderWalletID = senderWalletID;
            return this;
        }

        /**
         * Setter for the ReceiverWalletID of the Transaction to be built
         *
         * @param receiverWalletID the ID of the wallet of the receiver of the Transaction
         * @return this Builder
         */
        public Builder setReceiverWalletID(String receiverWalletID) {
            this.receiverWalletID = receiverWalletID;
            return this;
        }

        /**
         * Combo setter for the wallet IDs of the Transaction to be built
         *
         * @param senderWalletID   the sender wallet ID of the Transaction
         * @param receiverWalletID the receiver wallet ID of the Transaction
         * @return this Builder
         */
        public Builder setWalletIDs(String senderWalletID, String receiverWalletID) {
            return this.setSenderWalletID(senderWalletID).setReceiverWalletID(receiverWalletID);
        }

        /**
         * Setter for the ID of the sender of the Transaction to be built
         *
         * @param senderID the ID of the sender of the Transaction
         * @return this Builder
         */
        public Builder setSenderID(String senderID) {
            this.senderID = Optional.of(senderID);
            return this;
        }

        /**
         * Setter for the ID of the receiver of the Transaction to be built
         *
         * @param receiverID the ID of the receiver of the Transaction
         * @return this Builder
         */
        public Builder setReceiverID(String receiverID) {
            this.receiverID = Optional.of(receiverID);
            return this;
        }
    }
}
