package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 *  This is a helper class for some transaction / crypto related manipulation
 */
public class TransactionHelper {

    /**
     * Creates a raw transaction
     * @param web3Requests the web3 provider
     * @param from the hex address of the sender
     * @param to the hex address of the receiver
     * @param value the value to send
     * @return a completable future of the transaction as different values have
     * to be fetched from the blockchain to create one
     */
    public static CompletableFuture<RawTransaction> createTransaction(IWeb3Requests web3Requests, String from, String to, BigInteger value){
        CompletableFuture<BigInteger> nonce = web3Requests.getAccountNonce(from);
        CompletableFuture<BigInteger> gasPrice = web3Requests.getChainGasPrice();
        CompletableFuture<BigInteger> gasLimit = web3Requests.getChainGasLimit();
        return nonce.thenCompose(n -> gasPrice.thenCompose(gP -> gasLimit.thenApply(gL -> RawTransaction
                .createEtherTransaction(n, gP, gL, to, value))));
    }
}
