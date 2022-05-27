package com.sdp.swiftwallet.domain.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.sdp.swiftwallet.domain.model.Web3Requests;
import com.sdp.swiftwallet.domain.repository.web3.Web3ResponseType;

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
    @Test
    public void shouldBeAbleToGetGasPrice() {
        Web3Requests web3Requests = new Web3Requests();
        CompletableFuture<BigInteger> gasPromise = web3Requests.getChainGasPrice();
        BigInteger gasPrice = gasPromise.join();
        assertEquals(gasPrice, BigInteger.ONE);
    }

    @Test
    public void shouldBeAbleToGetGasLimit() {
        Web3Requests web3Requests = new Web3Requests();
        CompletableFuture<BigInteger> gasPromise = web3Requests.getChainGasLimit();
        BigInteger gasLimit = gasPromise.join();
        assertEquals(gasLimit, BigInteger.ONE);
    }

    @Test
    public void shouldBeAbleToGetAccountNonce() {
        Web3Requests web3Requests = new Web3Requests();
        CompletableFuture<BigInteger> noncePromise = web3Requests.getAccountNonce("0x0000");
        BigInteger accountNonce = noncePromise.join();
        assertEquals(accountNonce, BigInteger.ONE);
    }

    @Test
    public void shouldBeAbleToSendTransaction() {
        Web3Requests web3Requests = new Web3Requests();
        CompletableFuture<Web3ResponseType> respPromise = web3Requests.sendTransaction("0x0000");
        Web3ResponseType response = respPromise.join();
        assertEquals(response, Web3ResponseType.SUCCESS);
    }
}
