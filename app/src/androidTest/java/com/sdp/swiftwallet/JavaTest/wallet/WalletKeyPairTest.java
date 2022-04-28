package com.sdp.swiftwallet.JavaTest.wallet;
import android.util.Log;

import com.sdp.swiftwallet.JavaTest.mocks.MockWeb3Requests;
import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class WalletKeyPairTest {
    ECKeyPair ecKeyPair;
    KeyPairGenerator keyPairGenerator;
    public final static int mockID = 10;
    Web3Requests web3Requests;

    @Before
    public void init(){
        keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(SeedGenerator.generateSeed()));
        ecKeyPair = keyPairGenerator.generateKeyPair();
        web3Requests = new Web3Requests();
    }

    @Test
    public void creatingAnObjectShouldWork(){
        WalletKeyPair.fromKeyPair(ecKeyPair,mockID);
    }

    @Test
    public void idGetterShouldWork(){
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ecKeyPair, mockID);
        assert( walletKeyPair.getID() == mockID );
    }

    @Test
    public void nativeBalanceGetterShouldWork(){
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ecKeyPair, mockID);
        assert( walletKeyPair.getNativeBalance().equals(BigInteger.ZERO) );
    }

    @Test
    public void updatingBalanceShouldWork(){
        IWeb3Requests mockWeb3 = new MockWeb3Requests();
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ecKeyPair, mockID);
        walletKeyPair.updateBalance(mockWeb3);
        mockWeb3.getBalanceOf(walletKeyPair.getHexPublicKey()).join();
        BigInteger balance = walletKeyPair.getNativeBalance();
        assert(!balance.equals(BigInteger.ZERO));
    }
}
