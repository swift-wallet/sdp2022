package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

public class Wallets implements IWallets {

    private final ArrayList<WalletKeyPair> keyPairs;
    private final KeyPairGenerator keyPairGenerator;

    @Inject
    public Web3Requests web3Requests;

    private int counter = 0;

    public Wallets(String[] seed){
        long longSeed = SeedGenerator.stringSeedToLong(seed);
        keyPairGenerator = new KeyPairGenerator(longSeed);
        keyPairs = new ArrayList<>();
    }

    public Wallets(String[] seed, int to){
        this(seed);
        while(counter < to){
            generateWallet();
        }
    }

    public int generateWallet() {
        WalletKeyPair keyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair(), counter, web3Requests);
        keyPairs.add(keyPair);
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

    public void saveCounter(Context context) {
        SeedGenerator.saveCounter(context, counter);
    }

    public String[] getAddresses(){
        Object[] result = keyPairs.stream().map(WalletKeyPair::getHexPublicKey).toArray();
        return Arrays.copyOf(result, result.length, String[].class);
    }

    @Override
    public IWalletKeyPair[] getWallets() {
        IWalletKeyPair[] walletKeyPairs = new IWalletKeyPair[counter];
        return keyPairs.toArray(walletKeyPairs).clone();
    }
    @Override
    public IWalletKeyPair getWalletFromId(int id) {
        return keyPairs.get(id);
    }
}
