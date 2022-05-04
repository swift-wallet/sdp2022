//package com.sdp.swiftwallet.JavaTest.wallet;
//
//import android.content.Context;
//
//import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
//import com.sdp.swiftwallet.domain.model.wallet.IWallets;
//import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import java.math.BigInteger;
//import java.util.concurrent.CompletableFuture;
//
//@RunWith(JUnit4.class)
//public class WalletInterfacesTest {
//    @Test
//    public void shouldBeAbleToCreateIWallets(){
//        IWallets wallets = new IWallets() {
//            @Override
//            public IWalletKeyPair[] getWallets() {
//                return new IWalletKeyPair[0];
//            }
//
//            @Override
//            public IWalletKeyPair getWalletFromId(int id) {
//                return null;
//            }
//
//            @Override
//            public int getCounter() {
//                return 0;
//            }
//
//            @Override
//            public void saveCounter(Context context) {
//            }
//
//            @Override
//            public int generateWallet() {
//                return 0;
//            }
//
//            @Override
//            public String[] getAddresses() {
//                return new String[0];
//            }
//        };
//    }
//
//    @Test
//    public void shouldBeAbleToCreateIWalletKeyPair(){
//        IWalletKeyPair walletKeyPair = new IWalletKeyPair() {
//            @Override
//            public String getHexPublicKey() {
//                return null;
//            }
//
//            @Override
//            public int getID() {
//                return 0;
//            }
//
//            @Override
//            public BigInteger getNativeBalance() {
//                return null;
//            }
//
//            @Override
//            public void updateBalance() {
//
//            }
//        };
//    }
//
//    @Test
//    public void shouldBeAbleToCreateIWeb3Requests(){
//        IWeb3Requests web3Requests = new IWeb3Requests() {
//            @Override
//            public CompletableFuture<BigInteger> getBalanceOf(String hexAddress) {
//                return null;
//            }
//        };
//    }
//}
