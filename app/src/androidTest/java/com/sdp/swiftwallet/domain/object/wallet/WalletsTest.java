package com.sdp.swiftwallet.domain.object.wallet;
import com.sdp.swiftwallet.domain.model.object.wallet.WalletKeyPair;
import com.sdp.swiftwallet.domain.model.object.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.object.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.object.wallet.cryptography.SeedGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WalletsTest {
    private final static int mockCounter = 10;
    @Test
    public void creatingAnObjectShouldWork(){
        new Wallets(SeedGenerator.generateSeed());
        new Wallets(SeedGenerator.generateSeed(), mockCounter);
    }

    @Test
    public void counterShouldBeEqualToCreation(){
        Wallets wallets = new Wallets(SeedGenerator.generateSeed(), mockCounter);
        assert(wallets.getCounter() == mockCounter);
    }

    @Test
    public void generatingAWalletShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(seed));
        Wallets wallets = new Wallets(seed, 0);
        int id = wallets.generateWallet();
        assert(id == wallets.getCounter()-1);
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair(), id);
        assert(wallets.getWalletFromId(id).getHexPublicKey().equals(walletKeyPair.getHexPublicKey()));
    }
    @Test
    public void getCounterShouldWork(){
        Wallets wallets = new Wallets(SeedGenerator.generateSeed(), mockCounter);
        assert(mockCounter == wallets.getCounter());
    }
    @Test
    public void getWalletsShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(seed));
        Wallets wallets = new Wallets(seed, 0);
        int id = wallets.generateWallet();
        assert(id == wallets.getCounter()-1);
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair(), id);
        assert(wallets.getWallets()[0].getHexPublicKey().equals(walletKeyPair.getHexPublicKey()));
    }
    @Test
    public void getCurrentKeyPairShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(seed));
        Wallets wallets = new Wallets(seed, 1);
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair(), 0);
        assert(wallets.getCurrentKeyPair().getHexPublicKey().equals(walletKeyPair.getHexPublicKey()));
    }
    @Test
    public void creatingAZeroCounterWalletsShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        Wallets wallets = new Wallets(seed, 0);
        assert(wallets.getCurrentKeyPair() == null);
    }
}
