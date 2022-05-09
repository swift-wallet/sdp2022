package com.sdp.swiftwallet.domain.model.wallet.cryptography;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdp.swiftwallet.domain.model.wallet.Wallets;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

public class SeedGenerator {
    public static final int SEED_SIZE = 5;
    public static final String[] words =
            {"Plane", "Paper", "Bird", "Tree", "Knife", "Expensive", "Cheap", "Cheaper", "Inspiration",
            "Bad", "Mad", "Flat", "Squared", "Rectangle", "Guitar", "Secret", "Often", "This", "Large",
            "List", "Array", "Words", "Long", "Many", "Batman", "Spiderman", "Hulk", "Epfl", "Sciper",
            "Cap", "Arrow", "Bank", "Bitcoin", "Cactus", "Brother", "Hood", "Cat", "Dog", "Fish"};

    public static final String WALLETS_SHARED_PREFERENCES_NAME = "WALLETS";
    public static final String PREF_SEED_ID = "SEED";
    public static final String PREF_COUNTER_ID = "COUNTER";
    public static final String PREF_EXT_COUNTER_ID = "EXT_COUNTER";
    public static final String PREF_EXT_PK = "EXT_PK_";

    private String[] seed;
    public SeedGenerator(){
        seed = generateSeed();
    }

    public String getSeed() {
        return Arrays.stream(seed).reduce("", (a, b) -> {
            if( !a.equals("") ){
                return a + " " + b;
            }
            return b;
        });
    }

    public void reGenerateSeed(){
        seed = generateSeed();
    }

    public void saveSeed(Context context, String[] seed){
        if(seed.length != SEED_SIZE){
            throw new IllegalArgumentException("Bad seed length");
        }
        this.seed = seed;
        saveSeed(context);
    }
    private void saveSeed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_SEED_ID, toSavedSeedFormat()).apply();
    }

    private String toSavedSeedFormat() {
        return Arrays.stream(seed).reduce("", (a, b) -> {
            if( a.equals("") ){
                return b;
            }
            return a + "-" + b;
        });
    }

    public static void saveCounter(Context context , int counter, int extCounter){
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(PREF_COUNTER_ID, counter).putInt(PREF_EXT_PK, extCounter).apply();
    }

    public static void saveExternalWallet(Context context, int extCounter, BigInteger privKey) {
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_EXT_PK + String.valueOf(extCounter), privKey.toString()).apply();
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

        // Taking care of the externally imported wallets
        int extCounter = prefs.getInt(PREF_EXT_COUNTER_ID, 0);
        if(extCounter > 0){
            String[] privateKeys = new String[extCounter];
            for(int i=0; i<extCounter; i++) {
                privateKeys[i] = prefs.getString(PREF_EXT_PK + String.valueOf(i), null);
            }
            return new Wallets(seed, counter, extCounter, privateKeys);
        }

        return new Wallets(seed, counter);
    }

    public static String[] generateSeed() {
        SecureRandom secureRandom = new SecureRandom();
        String[] seed = new String[SEED_SIZE];
        int wordsLength = words.length;
        for( int i = 0; i < SEED_SIZE; i++ ){
            int index = Math.abs(secureRandom.nextInt());
            seed[i] = words[index % wordsLength];
        }
        return seed;
    }

    private static byte[] stringSeedToByteSeed(String[] seed){
        Optional<String> string = Arrays.stream(seed).reduce(String::concat);
        if(string.isPresent()){
            return string.get().getBytes(StandardCharsets.UTF_8);
        }else{
            throw new IllegalArgumentException("SeedGenerator: Error while creating the seed byte array");
        }
    }
    public static long stringSeedToLong(String[] seed){
        byte[] byteSeed = stringSeedToByteSeed(seed);
        long longSeed = 0;
        int seedLength = seed.length;
        for(int i=0; i<seed.length; i++){
            longSeed = longSeed + byteSeed[i];
        }
        return longSeed;
    }
}
