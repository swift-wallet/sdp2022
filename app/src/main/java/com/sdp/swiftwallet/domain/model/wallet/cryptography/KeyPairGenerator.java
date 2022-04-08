package com.sdp.swiftwallet.domain.model.wallet.cryptography;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.Random;

public class KeyPairGenerator {

    public static final int PRIVATE_KEY_SIZE = 32;

    private final Random random;
    private final long seed;

    public KeyPairGenerator(long seed){
        this.seed = seed;
        random = new Random(seed);
    }

    public ECKeyPair generateKeyPair(){
        byte[] privateKeyBytes = new byte[PRIVATE_KEY_SIZE];
        random.nextBytes(privateKeyBytes);
        BigInteger privateKey = ( new BigInteger(privateKeyBytes) ).abs();
        return ECKeyPair.create(privateKey);
    }
}
