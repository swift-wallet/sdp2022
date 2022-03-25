package com.sdp.swiftwallet.JavaTest.wallet;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.web3j.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class WalletKeyPairTest {
    ECKeyPair ecKeyPair;
    KeyPairGenerator keyPairGenerator;
    public final static int mockID = 10;
    @Before
    public void init(){
        keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(SeedGenerator.generateSeed()));
        ecKeyPair = keyPairGenerator.generateKeyPair();
    }

    @Test
    public void creatingAnObjectShouldWork(){
        WalletKeyPair walletKeyPair = new WalletKeyPair(ecKeyPair,mockID);
        walletKeyPair = new WalletKeyPair(ecKeyPair, "mock", mockID);
    }
}
