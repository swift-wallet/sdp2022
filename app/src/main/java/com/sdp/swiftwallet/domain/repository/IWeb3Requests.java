package com.sdp.swiftwallet.domain.repository;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface IWeb3Requests {
    CompletableFuture<BigInteger> getBalanceOf(String hexAddress);
    CompletableFuture<Web3ResponseType> sendTransaction(String hexValue);
    CompletableFuture<BigInteger> getChainGasPrice();
    CompletableFuture<BigInteger> getChainGasLimit();
    CompletableFuture<BigInteger> getAccountNonce(String hexAddress);
}
