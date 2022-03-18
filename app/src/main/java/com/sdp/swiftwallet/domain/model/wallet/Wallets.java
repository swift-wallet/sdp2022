package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class Wallets implements IWallets {
    private static final String WALLETS_SHARED_PREFERENCES_NAME = "WALLETS";
    private static final String PREF_SEED_ID = "SEED";
    private static final String PREF_COUNTER_ID = "COUNTER";

    private ArrayList<WalletKeyPair> keyPairs;
    private KeyPairGenerator keyPairGenerator;
    private int counter = 0;

    public Wallets(String[] seed){
        byte[] byteSeed = SeedGenerator.stringSeedToByteSeed(seed);
        keyPairGenerator = new KeyPairGenerator(byteSeed);
        keyPairs = new ArrayList<>();
    }

    private Wallets(String[] seed, int to){
        this(seed);
        while(counter < to){
            generateWallet();
        }
    }

    public static boolean hasSeed(Context context){
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.contains(PREF_SEED_ID);
    }

    public static Wallets recoverWallets(Context context){
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String[] seed;
        String seedJoined = prefs.getString(PREF_SEED_ID, null);
        seed = seedJoined.split("-");
        int counter = prefs.getInt(PREF_COUNTER_ID, 0);
        return new Wallets(seed, counter);
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
