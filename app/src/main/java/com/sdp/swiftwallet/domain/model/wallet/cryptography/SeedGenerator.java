package com.sdp.swiftwallet.domain.model.wallet.cryptography;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

public class SeedGenerator {
    public static final int SEED_SIZE = 5;
    public static final String[] words = {"Avion", "Papier", "Feuille", "Caillou", "Ciseau", "Oui", "Non", "Mot", "Inspiration"};

    private static final String WALLETS_SHARED_PREFERENCES_NAME = "WALLETS";
    private static final String PREF_SEED_ID = "SEED";
    private static final String PREF_COUNTER_ID = "COUNTER";

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

    public void saveSeed(Activity context, String[] seed){
        this.seed = seed;
        saveSeed(context);
    }
    private void saveSeed(Activity context) {
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Toast toast = new Toast(context);
        toast.setText(toSavedSeedFormat());
        toast.show();
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

    public static boolean hasSeed(Activity context){
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.contains(PREF_SEED_ID);
    }

    public static Wallets recoverWallets(Activity context){
        SharedPreferences prefs = context.getSharedPreferences(WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String[] seed;
        String seedJoined = prefs.getString(PREF_SEED_ID, null);
        seed = seedJoined.split("-");
        int counter = prefs.getInt(PREF_COUNTER_ID, 0);
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
    private static byte[] stringSeedToByteSeed(String seed){
        Optional<String> string = Arrays.stream(seed.split("-")).reduce(String::concat);
        if(string.isPresent()){
            return string.get().getBytes(StandardCharsets.UTF_8);
        }else{
            throw new IllegalArgumentException("SeedGenerator: Error while creating the seed byte array");
        }
    }
}
