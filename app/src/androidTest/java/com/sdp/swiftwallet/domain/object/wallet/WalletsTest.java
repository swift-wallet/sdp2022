package com.sdp.swiftwallet.domain.object.wallet;

import static org.junit.Assert.assertEquals;

import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class WalletsTest {
    private final static BigInteger mockPK = BigInteger.TEN;
    private final static int mockCounter = 10;
    @Test
    public void creatingAnObjectShouldWork(){
        new Wallets(SeedGenerator.generateSeed());
        new Wallets(SeedGenerator.generateSeed(), mockCounter);
    }

    @Test
    public void counterShouldBeEqualToCreation(){
        Wallets wallets = new Wallets(SeedGenerator.generateSeed(), mockCounter);
        assertEquals(wallets.getCounter(), mockCounter);
    }

    @Test
    public void generatingAWalletShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(seed));
        Wallets wallets = new Wallets(seed, 1);
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair());
        assertEquals(wallets.getCurrentKeyPair().getHexPublicKey(),(walletKeyPair.getHexPublicKey()));
    }
    @Test
    public void getCounterShouldWork(){
        Wallets wallets = new Wallets(SeedGenerator.generateSeed(), mockCounter);
        assertEquals(mockCounter,wallets.getCounter());
    }
    @Test
    public void getCurrentKeyPairShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(seed));
        Wallets wallets = new Wallets(seed, 1);
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair());
        assertEquals(wallets.getCurrentKeyPair().getHexPublicKey(),walletKeyPair.getHexPublicKey());
    }
    @Test
    public void getKeyPairByIdShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator(SeedGenerator.stringSeedToLong(seed));
        Wallets wallets = new Wallets(seed, 1);
        WalletKeyPair walletKeyPair = WalletKeyPair.fromKeyPair(keyPairGenerator.generateKeyPair());
        WalletKeyPair importedWalletKeyPair = WalletKeyPair.fromPrivateKey(mockPK.toString());
        wallets.importKeyPair(mockPK.toString());
        assertEquals(wallets.getWalletFromId(0).getHexPublicKey(),walletKeyPair.getHexPublicKey());
        assertEquals(wallets.getWalletFromId(1).getHexPublicKey(),importedWalletKeyPair.getHexPublicKey());
    }
    @Test
    public void creatingAZeroCounterWalletsShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        Wallets wallets = new Wallets(seed, 0);
        assert(wallets.getCurrentKeyPair() == null);
    }
}
