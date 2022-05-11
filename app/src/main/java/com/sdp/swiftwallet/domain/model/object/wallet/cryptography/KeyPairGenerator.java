package com.sdp.swiftwallet.domain.model.object.wallet.cryptography;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.Random;

/**
 * Generator of key at random given a seed
 */
public class KeyPairGenerator {
    
    private static final int PRIVATE_KEY_SIZE = 32;

    private final Random random;
    private final long seed;

    /**
     * Create a key generator given the see
     */
    public KeyPairGenerator(long seed){
        this.seed = seed;
        random = new Random(seed);
    }

    /**
     * Generate a private key
     */
    public ECKeyPair generateKeyPair(){
        byte[] privateKeyBytes = new byte[PRIVATE_KEY_SIZE];
        random.nextBytes(privateKeyBytes);
        BigInteger privateKey = ( new BigInteger(privateKeyBytes) ).abs();
        return ECKeyPair.create(privateKey);
    }
}
