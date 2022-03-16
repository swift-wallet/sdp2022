package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import java.util.ArrayList;

public class Wallets implements IWallets {
    private ArrayList<WalletKeyPair> keyPairs;
    private KeyPairGenerator keyPairGenerator;
    private int counter;
    protected Wallets(){
        this(SeedGenerator.generateSeed());
    }
    protected Wallets(String[] seed){
        byte[] byteSeed = SeedGenerator.stringSeedToByteSeed(seed);
        keyPairGenerator = new KeyPairGenerator(byteSeed);
    }
    protected Wallets(String[] seed, int to){
        byte[] byteSeed = SeedGenerator.stringSeedToByteSeed(seed);
        keyPairGenerator = new KeyPairGenerator(byteSeed);
        counter=0;
        for(; counter<to ; counter++){
            WalletKeyPair keyPair = new WalletKeyPair(keyPairGenerator.generateKeyPair(), counter);
            keyPairs.add(keyPair);
        }
    }

    @Override
    public WalletKeyPair[] getWallets() {
        return (WalletKeyPair[]) keyPairs.clone();
    }

    @Override
    public WalletKeyPair getWalletFromId(int id) {
        return keyPairs.get(id);
    }
}
