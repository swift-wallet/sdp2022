package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class TransactionCreator {
    public static CompletableFuture<RawTransaction> createTransaction(IWeb3Requests web3Requests, String from, String to, BigInteger value){
        CompletableFuture<BigInteger> nonce = web3Requests.getAccountNonce(from);
        CompletableFuture<BigInteger> gasPrice = web3Requests.getChainGasPrice();
        CompletableFuture<BigInteger> gasLimit = web3Requests.getChainGasLimit();
        return nonce.thenCompose(n -> gasPrice.thenCompose(gP -> gasLimit.thenApply(gL -> RawTransaction
                .createEtherTransaction(n, gP, gL, to, value))));
    }
}
