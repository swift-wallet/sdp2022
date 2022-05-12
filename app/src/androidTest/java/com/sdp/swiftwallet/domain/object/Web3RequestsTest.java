package com.sdp.swiftwallet.domain.object;

import com.sdp.swiftwallet.domain.model.Web3Requests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@RunWith(JUnit4.class)
public class Web3RequestsTest {
    public static final String DEAD_WALLET = "0x000000000000000000000000000000000000dEaD"; // can I have the private key pls ?
    @Test
    public void shouldBeAbleToGetFutureBalanceOf() {
        Web3Requests web3Requests = new Web3Requests();
        CompletableFuture<BigInteger> balancePromise = web3Requests.getBalanceOf(DEAD_WALLET);
        BigInteger balance = balancePromise.join();
        assert (!balance.equals(BigInteger.ZERO));
    }
}
