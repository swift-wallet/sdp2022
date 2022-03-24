package com.sdp.swiftwallet.wallet;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.KeyPairGenerator;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WalletsTest {
    private final static int mockCounter = 10;
    @Test
    public void creatingAnObjectShouldWork(){
        Wallets wallets = new Wallets(SeedGenerator.generateSeed());
        wallets = new Wallets(SeedGenerator.generateSeed(), mockCounter);
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
        WalletKeyPair walletKeyPair = new WalletKeyPair(keyPairGenerator.generateKeyPair(), id);
        assert(wallets.getWalletFromId(id).getHexPublicKey().equals(walletKeyPair.getHexPublicKey()));
    }
}
