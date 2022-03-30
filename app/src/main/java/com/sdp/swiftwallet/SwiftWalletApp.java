package com.sdp.swiftwallet;

import android.app.Application;

import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.FirebaseTransactionHistoryProducer;

import java.util.ArrayList;
import java.util.List;

public class SwiftWalletApp extends Application {
    private FirebaseTransactionHistoryProducer transactionHistoryProducer;

    List<User> userDatabase = new ArrayList<>();
    //Indice of the user in database
    int currentlyLogged;

    public Boolean isEmpty(){
        return userDatabase.isEmpty();
    }

    public Boolean isPresent(User u){
        return userDatabase.contains(u);
    }

    /**
     * Add and update the user's currently logged indic in the database
     * @param u
     * @return
     */
    public int addAndUpdate(User u){
        if (isPresent(u)) {
            currentlyLogged = userDatabase.indexOf(u);
        } else {
            if (isEmpty()) { currentlyLogged = 0;
            } else { currentlyLogged += 1; }
            userDatabase.add(u);
        }
        return currentlyLogged;
    }

    /**
     * returns the user with index i
     */
    public User getUser(int i){
        if (i < 0 || i > userDatabase.size()) throw new IllegalArgumentException();
        return userDatabase.get(i);
    }

    /**
     * returns index of currently logged user
     */
    public int getLoggedUserIndex(){
        return currentlyLogged;
    }

    /**
     * Getter for the TransactionHistoryProducer
     *
     * @return the TransactionHistoryProducer
     */
    public FirebaseTransactionHistoryProducer getTransactionHistoryProducer() {
        return transactionHistoryProducer;
    }

    /**
     * Setter for the TransactionHistoryProducer
     *
     * @param transactionHistoryProducer the new TransactionHistoryProducer
     */
    public void setTransactionHistoryProducer(FirebaseTransactionHistoryProducer transactionHistoryProducer) {
        this.transactionHistoryProducer = transactionHistoryProducer;
    }
}
