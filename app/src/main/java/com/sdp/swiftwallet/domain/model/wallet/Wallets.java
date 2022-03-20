package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class Wallets implements IWallets {

    private final ArrayList<WalletKeyPair> keyPairs;
    private final KeyPairGenerator keyPairGenerator;
    private int counter = 0;

    public Wallets(String[] seed){
        byte[] byteSeed = SeedGenerator.stringSeedToByteSeed(seed);
        keyPairGenerator = new KeyPairGenerator(byteSeed);
        keyPairs = new ArrayList<>();
    }

    public Wallets(String[] seed, int to){
        this(seed);
        while(counter < to){
            generateWallet();
        }
    }

    public int generateWallet() {
        WalletKeyPair keyPair = new WalletKeyPair(keyPairGenerator.generateKeyPair(), counter);
        keyPairs.add(keyPair);
        return counter++;
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
