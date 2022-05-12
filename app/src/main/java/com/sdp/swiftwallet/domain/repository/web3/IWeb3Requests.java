package com.sdp.swiftwallet.domain.repository.web3;

import com.sdp.swiftwallet.domain.repository.web3.Web3ResponseType;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * Represents behavior for blockchain web requests
 */
public interface IWeb3Requests {
    CompletableFuture<BigInteger> getBalanceOf(String hexAddress);
    CompletableFuture<Web3ResponseType> sendTransaction(String hexValue);
    CompletableFuture<BigInteger> getChainGasPrice();
    CompletableFuture<BigInteger> getChainGasLimit();
    CompletableFuture<BigInteger> getAccountNonce(String hexAddress);
}
