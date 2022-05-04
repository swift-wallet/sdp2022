package com.sdp.swiftwallet.JavaTest.mocks;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

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
}
