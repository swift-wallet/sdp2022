package com.sdp.swiftwallet.domain.object.wallet;

import com.sdp.swiftwallet.domain.mocks.MockWeb3Requests;
import com.sdp.swiftwallet.domain.model.Web3Requests;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;

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
        WalletKeyPair.fromKeyPair(ecKeyPair);
    }

    @Test
    public void nativeBalanceGetterShouldWork(){
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ecKeyPair);
        assert( walletKeyPair.getNativeBalance().equals(BigInteger.ZERO) );
    }

    @Test
    public void updatingBalanceShouldWork(){
        IWeb3Requests mockWeb3 = new MockWeb3Requests();
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ecKeyPair);
        walletKeyPair.updateBalance(mockWeb3);
        mockWeb3.getBalanceOf(walletKeyPair.getHexPublicKey()).join();
        BigInteger balance = walletKeyPair.getNativeBalance();
        assert(!balance.equals(BigInteger.ZERO));
    }
    @Test
    public void shouldBeAbleToSign(){
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ecKeyPair);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                "to",
                "data"
        );
        walletKeyPair.signTransaction(rawTransaction);
    }
}
