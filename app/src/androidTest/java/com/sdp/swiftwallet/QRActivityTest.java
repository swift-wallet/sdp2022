package com.sdp.swiftwallet;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;

import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.clearText;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo;
import static com.adevinta.android.barista.interaction.BaristaKeyboardInteractions.closeKeyboard;

import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.QRCodeGenerator;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QRActivityTest {
    private final static String validString = "TOQR";
    @Rule
    public ActivityScenarioRule<QRActivity> testRule = new ActivityScenarioRule<>(QRActivity.class);

    @Test
    public void successfullyLaunchesScanningActivityWhenButtonPressed() {
        Intents.init();

        clickOn(R.id.qr_button);

        intended(hasAction("com.google.zxing.client.android.SCAN"));

        Intents.release();
    }

    @Test
    public void successfullyCreateAQRCodeWhenButtonPressed(){
        clearText(R.id.qr_result_edittext);
        typeTo(R.id.qr_result_edittext, validString);
        closeKeyboard();
        clickOn(R.id.qr_button_generate_qr);
        try{
            Bitmap expectedBitmap = QRCodeGenerator.encodeAsBitmap(validString);
            testRule.getScenario().onActivity((activity) -> {
                Bitmap showedBitmap = ((BitmapDrawable)((ImageView)activity.findViewById(R.id.qr_imageView)).getDrawable()).getBitmap();
                assertTrue(showedBitmap.sameAs(expectedBitmap));
            });
        }catch (Exception e){
            Assert.fail("Exception was thrown : "+ e.getMessage());
        }

    }

}