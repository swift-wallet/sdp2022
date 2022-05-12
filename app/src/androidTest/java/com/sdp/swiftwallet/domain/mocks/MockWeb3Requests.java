package com.sdp.swiftwallet.domain.mocks;

import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;
import com.sdp.swiftwallet.domain.repository.web3.Web3ResponseType;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class MockWeb3Requests implements IWeb3Requests {
    public BigInteger mockBalance = BigInteger.ONE;
    public CompletableFuture<BigInteger> mockFuture;
    public MockWeb3Requests(){
        mockFuture = CompletableFuture.completedFuture(mockBalance);
    }
    @Override
    public CompletableFuture<BigInteger> getBalanceOf(String hexAddress) {
        return mockFuture;
    }

    @Override
    public CompletableFuture<Web3ResponseType> sendTransaction(String hexValue) {
        return
                CompletableFuture.completedFuture(Web3ResponseType.SUCCESS);
    }

    @Override
    public CompletableFuture<BigInteger> getChainGasPrice() {
        return CompletableFuture.completedFuture(BigInteger.ONE);
    }

    @Override
    public CompletableFuture<BigInteger> getChainGasLimit() {
        return CompletableFuture.completedFuture(BigInteger.ONE);
    }

    @Override
    public CompletableFuture<BigInteger> getAccountNonce(String hexAddress) {
        return CompletableFuture.completedFuture(BigInteger.ONE);
    }
}
