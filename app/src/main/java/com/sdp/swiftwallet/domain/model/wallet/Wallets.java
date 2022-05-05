package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * This is the main wallets manager, it handles:
 *  -Wallet creation
 *  -Wallets access
 *  -Seed counter saving
 */
public class Wallets implements IWallets {

    private final ArrayList<WalletKeyPair> keyPairsList;
    private final HashMap<String, WalletKeyPair> keyPairsMap;
    private final KeyPairGenerator keyPairGenerator;
    private IWalletKeyPair currentKeyPair = null;

    private int counter = 0;

    public Wallets(String[] seed){
        long longSeed = SeedGenerator.stringSeedToLong(seed);
        keyPairGenerator = new KeyPairGenerator(longSeed);
        keyPairsList = new ArrayList<>();
        keyPairsMap = new HashMap<>();
    }

    public Wallets(String[] seed, int to){
        this(seed);
        if(to > 0){
            while(counter < to){
                generateWallet();
            }
            currentKeyPair = keyPairsList.get(to - 1); // Set the last wallet as default current wallet
        }
    }

    public int generateWallet() {
        WalletKeyPair keyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair(), counter);
        addKeyPair(keyPair);
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

    public void saveCounter(Context context) {
        SeedGenerator.saveCounter(context, counter);
    }

    public String[] getAddresses(){
        Object[] result = keyPairsList.stream().map(WalletKeyPair::getHexPublicKey).toArray();
        return Arrays.copyOf(result, result.length, String[].class);
    }

    @Override
    public IWalletKeyPair[] getWallets() {
        IWalletKeyPair[] walletKeyPairs = new IWalletKeyPair[counter];
        return keyPairsList.toArray(walletKeyPairs).clone();
    }
    @Override
    public IWalletKeyPair getWalletFromId(int id) {
        return keyPairsList.get(id);
    }

    public IWalletKeyPair getWalletFromAddress(String address) {
        return keyPairsMap.get(address);
    }

    private void addKeyPair(WalletKeyPair walletKeyPair) {
        keyPairsList.add(walletKeyPair);
        keyPairsMap.put(walletKeyPair.getHexPublicKey(), walletKeyPair);
    }

    public void setCurrentKeyPair(IWalletKeyPair currentKeyPair) {
        this.currentKeyPair = currentKeyPair;
    }
    public IWalletKeyPair getCurrentKeyPair() {
        return currentKeyPair;
    }
}
