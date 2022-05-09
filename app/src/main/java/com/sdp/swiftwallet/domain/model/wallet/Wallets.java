package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This is the main wallets manager, it handles:
 *  -Wallet creation
 *  -Wallets access
 *  -Seed counter saving
 */
public class Wallets implements IWallets {

    // This contains only the seed generated key pairs
    private final ArrayList<WalletKeyPair> keyPairsList;
    // This contains only the externally imported key pairs
    private final ArrayList<WalletKeyPair> extKeyPairsList;
    // This contains both
    private final HashMap<String, WalletKeyPair> keyPairsMap;

    private final KeyPairGenerator keyPairGenerator;
    private IWalletKeyPair currentKeyPair = null;

    private int extCounter = 0;
    private int counter = 0;

    public Wallets(String[] seed){
        long longSeed = SeedGenerator.stringSeedToLong(seed);
        keyPairGenerator = new KeyPairGenerator(longSeed);
        keyPairsList = new ArrayList<>();
        keyPairsMap = new HashMap<>();
        extKeyPairsList = new ArrayList<>();
    }

    public Wallets(String[] seed, int _counter){
        this(seed);
        if(_counter > 0){
            while(counter < _counter){
                generateWallet();
            }
            currentKeyPair = keyPairsList.get(_counter - 1); // Set the last wallet as default current wallet
        }
    }
    public Wallets(String[] seed, int _counter, int _extCounter, String[] privateKeys){
        this(seed);
        if(_counter > 0){
            while(counter < _counter){
                generateWallet();
            }
            currentKeyPair = keyPairsList.get(_counter - 1); // Set the last wallet as default current wallet
        }
        for(int i=0; i<_extCounter; i++){
            importKeyPair(privateKeys[i]);
        }
    }

    public int generateWallet() {
        WalletKeyPair keyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair());
        addKeyPair(keyPair);
        return counter++;
    }

    public int getCounter() {
        return counter + extCounter;
    }

    public void saveCounter(Context context) {
        SeedGenerator.saveCounter(context, counter, extCounter);
    }

    public String[] getAddresses(){
        Object[] seedG = keyPairsList.stream().map(WalletKeyPair::getHexPublicKey).toArray();
        Object[] extG = extKeyPairsList.stream().map(WalletKeyPair::getHexPublicKey).toArray();
        String[] result = Arrays.copyOf(seedG, seedG.length + extG.length, String[].class);
        System.arraycopy(extG, 0, result, seedG.length, extG.length);
        return result;
    }

    public IWalletKeyPair getWalletFromAddress(String address) {
        return keyPairsMap.get(address);
    }

    /**
     * Adds a wallet key pair to the seed generated data structures
     * @param walletKeyPair the wallet key pair to add
     */
    private void addKeyPair(WalletKeyPair walletKeyPair) {
        keyPairsList.add(walletKeyPair);
        keyPairsMap.put(walletKeyPair.getHexPublicKey(), walletKeyPair);
    }

    public IWalletKeyPair getWalletFromId(int id){
        if(id >= counter){
            return extKeyPairsList.get(id % counter);
        }
        return keyPairsList.get(id);
    }

    /**
     * Adds a wallet key pair to the imported data structures
     * @param privateKey the private key of the imported wallet
     */
    public void importKeyPair(String privateKey) {
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(ECKeyPair.create(new BigInteger(privateKey)));
        extKeyPairsList.add(walletKeyPair);
        keyPairsMap.put(walletKeyPair.getHexPublicKey(), walletKeyPair);
        extCounter++;
    }

    public void setCurrentKeyPair(IWalletKeyPair currentKeyPair) {
        this.currentKeyPair = currentKeyPair;
    }
    public IWalletKeyPair getCurrentKeyPair() {
        return currentKeyPair;
    }
}
