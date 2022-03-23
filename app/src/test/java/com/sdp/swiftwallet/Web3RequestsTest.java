package com.sdp.swiftwallet;

import android.util.Log;

import com.sdp.swiftwallet.data.repository.Web3Requests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(JUnit4.class)
public class Web3RequestsTest {
    public static final String deadWallet = "0x000000000000000000000000000000000000dEaD"; // can I have the private key pls ?
    @Test
    public void shouldBeAbleToGetBalanceOf(){
        Web3Requests web3Requests = new Web3Requests();
        CompletableFuture<BigInteger> balancePromise = web3Requests.getBalanceOf(deadWallet);
        BigInteger balance = balancePromise.join();
        assert( !balance.equals(BigInteger.ZERO) );
    }
}
