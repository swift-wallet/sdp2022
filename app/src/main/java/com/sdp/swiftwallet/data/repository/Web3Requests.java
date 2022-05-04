package com.sdp.swiftwallet.data.repository;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
import com.sdp.swiftwallet.domain.repository.Web3ResponseType;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * Represent a blockchain web request
 */
public class Web3Requests implements IWeb3Requests {

    // Infura API for accessing to ETH and IPFS networks
    private final static String INFURA_API = "https://mainnet.infura.io/v3/33d8d39a78464db1b58bdd016f274760";
    private final Web3j web3;

    /**
     * Constructs a request given INFURA t
     */
    public Web3Requests(){
        web3 = Web3j.build(new HttpService(INFURA_API));
    }

    /**
     * Gets the ethereum balance of a hex format ETH address
     * @param hexAddress hex format ethereum address
     * @return ETH balance
     */
    public CompletableFuture<BigInteger> getBalanceOf(String hexAddress) {
        return web3
                .ethGetBalance(hexAddress, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .thenApply(EthGetBalance::getBalance);
    }

    /**
     * Send a transaction to hex format ETH address
     * @param hexValue hex format ETH address
     * @return result WebResponse of the transaction
     */
    @Override
    public CompletableFuture<Web3ResponseType> sendTransaction(String hexValue) {
        return null;
    }

    /**
     * Gets the chain gas price
     * @return
     */
    @Override
    public CompletableFuture<BigInteger> getChainGasPrice() {
        return CompletableFuture.completedFuture(BigInteger.ONE);
    }

    /**
     *
     * @return
     */
    @Override
    public CompletableFuture<BigInteger> getChainGasLimit() {
        return CompletableFuture.completedFuture(BigInteger.ONE);
    }

    /**
     * @param hexAddress
     * @return nonce of
     */
    @Override
    public CompletableFuture<BigInteger> getAccountNonce(String hexAddress) {
            return CompletableFuture.completedFuture(BigInteger.ONE);
    }
}
