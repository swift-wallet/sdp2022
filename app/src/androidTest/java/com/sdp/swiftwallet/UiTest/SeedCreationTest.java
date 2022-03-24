package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.clearText;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo;
import static com.adevinta.android.barista.interaction.BaristaKeyboardInteractions.closeKeyboard;

import static org.hamcrest.Matchers.not;
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
        clearText(R.id.seed_view);
        clickOn(R.id.generate_seed_button);
        onView(withId(R.id.seed_view)).check(matches(not(withText(""))));
    }
}
