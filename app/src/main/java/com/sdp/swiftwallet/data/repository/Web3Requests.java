package com.sdp.swiftwallet.data.repository;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class Web3Requests {
    private final static String INFURA_API = "https://mainnet.infura.io/v3/33d8d39a78464db1b58bdd016f274760";
    private Web3j web3;

    public Web3Requests(){
        web3 = Web3j.build(new HttpService(INFURA_API));
    }
    // Gets the ethereum balance of a hex format ethereum address
    public CompletableFuture<BigInteger> getFutureBalanceOf(String hexAddress) {
        return web3
                .ethGetBalance(hexAddress, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .thenApply(EthGetBalance::getBalance);
    }

    public BigInteger getBalanceOf(String hexAddress) throws IOException {
        return web3
                .ethGetBalance(hexAddress, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance();
    }
}
