package com.sdp.swiftwallet.data.repository;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
import com.sdp.swiftwallet.domain.repository.Web3ResponseType;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class Web3Requests implements IWeb3Requests {
    private final static String INFURA_API = "https://mainnet.infura.io/v3/33d8d39a78464db1b58bdd016f274760";
    private final Web3j web3;

    public Web3Requests(){
        web3 = Web3j.build(new HttpService(INFURA_API));
    }
    // Gets the ethereum balance of a hex format ethereum address
    public CompletableFuture<BigInteger> getBalanceOf(String hexAddress) {
        return web3
                .ethGetBalance(hexAddress, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .thenApply(EthGetBalance::getBalance);
    }

    @Override
    public CompletableFuture<Web3ResponseType> sendTransaction(String hexValue) {
        return null;
    }

    @Override
    public CompletableFuture<BigInteger> getChainGasPrice() {
        return null;
    }

    @Override
    public CompletableFuture<BigInteger> getChainGasLimit() {
        return null;
    }

    @Override
    public CompletableFuture<BigInteger> getAccountNonce(String hexAddress) {
        return null;
    }
}
