package com.sdp.swiftwallet.domain.repository;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface IWeb3Requests {
    CompletableFuture<BigInteger> getBalanceOf(String hexAddress);
}
