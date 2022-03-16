package com.sdp.swiftwallet.domain.model.wallet.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

public class SeedGenerator {
    public static final int SEED_SIZE = 5;
    public static final String[] words = {"Avion", "Papier", "Feuille", "Caillou", "Ciseau", "Oui", "Non", "Mot", "Inspiration"};

    public static String[] generateSeed() {
        SecureRandom secureRandom = new SecureRandom();
        String[] seed = new String[SEED_SIZE];
        int wordsLength = words.length;
        for( int i = 0; i < SEED_SIZE; i++ ){
            int index = secureRandom.nextInt();
            seed[i] = words[index % wordsLength];
        }
        return seed;
    }

    public static byte[] stringSeedToByteSeed(String[] seed){
        Optional<String> string = Arrays.stream(seed).reduce(String::concat);
        if(string.isPresent()){
            return string.get().getBytes(StandardCharsets.UTF_8);
        }else{
            throw new IllegalArgumentException("SeedGenerator: Error while creating the seed byte array");
        }
    }
}
