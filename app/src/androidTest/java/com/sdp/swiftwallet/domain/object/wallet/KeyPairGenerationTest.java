package com.sdp.swiftwallet.domain.object.wallet;
import com.sdp.swiftwallet.domain.model.object.wallet.cryptography.KeyPairGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.web3j.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class KeyPairGenerationTest {
    public static final long mockSeed = 1987865745;

    @Test
    public void creatingAGeneratorShouldWork(){
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(mockSeed);
    }

    @Test
    public void generatingKeyPairShouldWork(){
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(mockSeed);
        ECKeyPair ecKeyPair = keyPairGenerator.generateKeyPair();
    }

    @Test
    public void generatingKeyPairWithSameSeedShouldEqual(){
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(mockSeed);
        ECKeyPair ecKeyPair = keyPairGenerator.generateKeyPair();

        keyPairGenerator = new KeyPairGenerator(mockSeed);
        ECKeyPair ecKeyPair1 = keyPairGenerator.generateKeyPair();

        assert(ecKeyPair.getPublicKey().equals(ecKeyPair1.getPublicKey()));
    }


}
