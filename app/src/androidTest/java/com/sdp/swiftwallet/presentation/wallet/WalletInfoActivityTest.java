package com.sdp.swiftwallet.presentation.wallet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.qrCode.QRCodeGenerator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class WalletInfoActivityTest {
    Intent testIntent;
    public static String mockBalance = "12.32455";
    public static String mockAddress = "0x000000000000000000000000000000000000dead";

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Before
    public void setupIntent() {
        testIntent = new Intent(ApplicationProvider.getApplicationContext(), WalletInfoActivity.class);
        testIntent.putExtra(
                WalletInfoActivity.BALANCE_EXTRA,
                mockBalance
        );
        testIntent.putExtra(
                WalletInfoActivity.ADDRESS_EXTRA,
                mockAddress
        );
    }
    @Test
    public void shouldBeAbleToLaunchActivity(){
        try (ActivityScenario<WalletInfoActivity> scenario = ActivityScenario.launch(testIntent)) {
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void shouldSetInformationsProperly(){
        ActivityScenario<WalletInfoActivity> activityScenario = ActivityScenario.launch(testIntent);
        onView(withId(R.id.wallet_info_address)).check(matches(withText(mockAddress)));
        onView(withId(R.id.wallet_info_balance)).check(matches(withText(mockBalance)));
        activityScenario.onActivity(activity -> {
            try{
                Bitmap expectedBitmap = QRCodeGenerator.encodeAsBitmap(mockAddress);
                Bitmap showedBitmap = ((BitmapDrawable)((ImageView)activity.findViewById(R.id.qr_info_view)).getDrawable()).getBitmap();
                assert(showedBitmap.sameAs(expectedBitmap));
            }catch (Exception e){
                Assert.fail("Exception was thrown : "+ e.getMessage());
            }
        });

    }
}
