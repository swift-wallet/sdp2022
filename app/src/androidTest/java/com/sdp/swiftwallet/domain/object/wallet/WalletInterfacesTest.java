package com.sdp.swiftwallet.domain.object.wallet;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.sdp.swiftwallet.domain.model.Web3Requests;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.IWallets;
import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;
import com.sdp.swiftwallet.domain.repository.web3.Web3ResponseType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@RunWith(JUnit4.class)
public class WalletInterfacesTest {
    @Test
    public void shouldBeAbleToCreateIWallets(){
        IWallets wallets = new IWallets() {
            @Override
            public int getCounter() {
                return 0;
            }

            @Override
            public void saveCounter(Context context) {
            }

            @Override
            public IWalletKeyPair generateWallet() {
                return null;
            }

            @Override
            public String[] getAddresses() {
                return new String[0];
            }

            @Override
            public void importKeyPair(String privateKey) {
            }

            @Override
            public IWalletKeyPair importKeyPair(Context context, String privateKey) {
                return null;
            }
        };
        wallets.importKeyPair("0");
        wallets.getCounter();
        wallets.saveCounter(ApplicationProvider.getApplicationContext());
        wallets.generateWallet();
        wallets.getAddresses();
    }

    @Test
    public void shouldBeAbleToCreateIWalletKeyPair(){
        IWalletKeyPair walletKeyPair = new IWalletKeyPair() {
            @Override
            public String getHexPublicKey() {
                return null;
            }

            @Override
            public BigInteger getNativeBalance() {
                return null;
            }

            @Override
            public void updateBalance(IWeb3Requests web3Requests) {
            }

            @Override
            public String signTransaction(RawTransaction rawTransaction) {
                return null;
            }
        };
        walletKeyPair.getHexPublicKey();
        walletKeyPair.getNativeBalance();
        walletKeyPair.updateBalance(new Web3Requests());
        walletKeyPair.signTransaction(RawTransaction.createTransaction(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, "ll", "ll"));
    }

    @Test
    public void shouldBeAbleToCreateIWeb3Requests(){
        IWeb3Requests web3Requests = new IWeb3Requests() {
            @Override
            public CompletableFuture<BigInteger> getBalanceOf(String hexAddress) {
                return null;
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
        };
        web3Requests.getBalanceOf("account");
        web3Requests.sendTransaction("hex");
        web3Requests.getChainGasPrice();
        web3Requests.getChainGasLimit();
        web3Requests.getAccountNonce("add");
    }
}
