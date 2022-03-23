package com.sdp.swiftwallet.JavaTest.wallet.cryptography;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SeedGenerationTest {

    public final static String mockSeed = "test-testouille";
    public final static String mockSpaceSeed = "test testouille";
    public final static String[] mockArraySeed = new String[]{"test", "testouille"};

    public final static int mockCounter = 10;

    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);

    public void setValidSeedAndCounter(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(SeedGenerator.PREF_SEED_ID, mockSeed)
                .putInt(SeedGenerator.PREF_COUNTER_ID, mockCounter)
                .apply();
    }
    public void resetPrefs(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public String getSeed(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SeedGenerator.PREF_SEED_ID, null);
    }

    @Before
    public void init(){
        testRule.getScenario().onActivity(activity -> {
            resetPrefs(activity);
        });
    }

    @Test
    public void hasSeedFalseWhenNotSet() {
        testRule.getScenario().onActivity(activity -> {
           boolean hasSeed = SeedGenerator.hasSeed(activity);
           assertFalse(hasSeed);
        });
    }

    @Test
    public void hasSeedTrueWhenSet(){
        testRule.getScenario().onActivity(activity -> {
            setValidSeedAndCounter(activity);
            boolean hasSeed = SeedGenerator.hasSeed(activity);
            assert(hasSeed);
        });
    }
    @Test
    public void recoverWalletsWorksWhenSet(){
        testRule.getScenario().onActivity(activity -> {
            setValidSeedAndCounter(activity);
            SeedGenerator.recoverWallets(activity);
        });
    }

    @Test
    public void recoverWalletsThrowsNullWhenNotSet(){
        testRule.getScenario().onActivity(activity -> {
            try{
                SeedGenerator.recoverWallets(activity);
                fail();
            }catch(Exception e){
                if( !(e instanceof NullPointerException) ){
                    fail();
                }
            }
        });
    }

    @Test
    public void generateSeedShouldWork(){
        String[] seed = SeedGenerator.generateSeed();
        assert(seed.length == SeedGenerator.SEED_SIZE);
        for (int i=0; i<SeedGenerator.SEED_SIZE; i++){
            assert(!seed[i].equals(""));
        }
    }

    @Test
    public void stringSeedToLongShouldWork(){
        long longSeed = SeedGenerator.stringSeedToLong(SeedGenerator.generateSeed());
        assert(longSeed != 0);
    }

    @Test
    public void stringSeedToLongShouldFailWithEmptySeed(){
        try{
            SeedGenerator.stringSeedToLong(new String[0]);
        }catch(Exception e){
            if(!(e instanceof IllegalArgumentException)){
                fail();
            }
        }
    }
    @Test
    public void getSeedShouldWork(){
        testRule.getScenario().onActivity(activity -> {
            setValidSeedAndCounter(activity);
            SeedGenerator seedGenerator = new SeedGenerator();
            seedGenerator.saveSeed(activity, mockArraySeed);
            assert(getSeed(activity).equals(mockSeed));
            assert(seedGenerator.getSeed().equals(mockSpaceSeed));
        });
    }
}
