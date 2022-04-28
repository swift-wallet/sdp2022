package com.sdp.swiftwallet.JavaTest.mocks;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
import com.sdp.swiftwallet.domain.repository.Web3ResponseType;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import jnr.posix.CmsgHdr;

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
        return null;
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
