package com.sdp.swiftwallet.UiTest;

import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.clearText;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo;
import static com.adevinta.android.barista.interaction.BaristaKeyboardInteractions.closeKeyboard;

import static org.junit.Assert.fail;

import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SeedCreationTest {
    public static final String badSeed = "AA B";
    public static final String correctSeed = "add juu ss ll dd";
    @Rule
    public ActivityScenarioRule<CreateSeedActivity> testRule = new ActivityScenarioRule<>(CreateSeedActivity.class);

    @Test
    public void shouldGenerateASeedOnStart(){
        testRule.getScenario().onActivity(activity -> {
            EditText seedView = (EditText)activity.findViewById(R.id.seed_view);
            String seed = seedView.getText().toString();
            assert(!seed.equals(""));
            String[] generatedSeed = seed.split(" ");
            assert(generatedSeed.length == SeedGenerator.SEED_SIZE);
        });
    }
    @Test
    public void shouldGenerateANewSeedOnClick(){
        testRule.getScenario().onActivity(activity -> {
            EditText seedView = (EditText)activity.findViewById(R.id.seed_view);
            String seed = seedView.getText().toString();
            clickOn(R.id.generate_seed_button);
            assert(!seedView.getText().toString().equals(seed));
        });
    }

    @Test
    public void shouldFailWhenSavingABadSeed(){
        clearText(R.id.seed_view);
        typeTo(R.id.seed_view, badSeed);
        closeKeyboard();
        try{
            clickOn(R.id.save_seed_button);
        }catch(Exception e){
            return;
        }
        fail();
    }
    @Test
    public void shouldSuccessWhenSavingACorrectSeed(){
        clearText(R.id.seed_view);
        typeTo(R.id.seed_view, correctSeed);
        closeKeyboard();
        try{
            clickOn(R.id.save_seed_button);
        }catch(Exception e){
            fail();
        }
    }

}
