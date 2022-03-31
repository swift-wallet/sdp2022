package com.sdp.swiftwallet.domain.repository;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface IWeb3Requests {
    public CompletableFuture<BigInteger> getBalanceOf(String hexAddress);
}
