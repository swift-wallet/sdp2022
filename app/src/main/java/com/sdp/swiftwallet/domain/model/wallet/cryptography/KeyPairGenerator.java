package com.sdp.swiftwallet.domain.model.wallet.cryptography;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class KeyPairGenerator {

    public static final int PRIVATE_KEY_SIZE = 32;

    private SecureRandom secureRandom;
    private byte[] seed;

    public KeyPairGenerator(byte[] seed){
        this.seed = seed;
        secureRandom = new SecureRandom(seed);
    }

    public ECKeyPair generateKeyPair(){
        byte[] privateKeyBytes = new byte[PRIVATE_KEY_SIZE];
        secureRandom.nextBytes(privateKeyBytes);
        BigInteger privateKey = ( new BigInteger(privateKeyBytes) ).abs();
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        return keyPair;
    }
}
