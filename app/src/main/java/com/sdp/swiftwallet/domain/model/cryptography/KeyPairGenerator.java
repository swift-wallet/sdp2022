package com.sdp.swiftwallet.domain.model.cryptography;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class KeyPairGenerator {

    public static final int PRIVATE_KEY_SIZE = 32;

    private SecureRandom secureRandom;
    private byte[] seed;
    private int counter;
    private ArrayList<ECKeyPair> keyPairs;

    public KeyPairGenerator(byte[] seed){
        this.seed = seed;
        secureRandom = new SecureRandom(seed);
        counter = 0;
        keyPairs = new ArrayList<>();
    }

    public KeyPairGenerator(byte[] seed, int to){
        this.seed = seed;
        secureRandom = new SecureRandom(seed);
        counter = 0;
        keyPairs = new ArrayList<>();
        for( int i = 0 ; i < to; i++ ){
            generateKeyPair();
        }
    }

    public ECKeyPair generateKeyPair(){
        byte[] privateKeyBytes = new byte[PRIVATE_KEY_SIZE];
        secureRandom.nextBytes(privateKeyBytes);
        BigInteger privateKey = ( new BigInteger(privateKeyBytes) ).abs();
        counter ++;
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        keyPairs.add( keyPair );
        return keyPair;
    }

    public ECKeyPair getKeyPair(int id){
        if(counter < id){
            throw new IllegalArgumentException("KeyPairGenerator: Id is not valid");
        }
        return keyPairs.get(id);
    }
}
