package com.sdp.swiftwallet.presentation.signIn;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;

import org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator.PartyU;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> registerScenario = new ActivityScenarioRule<>(RegisterActivity.class);

    // Counting idling resource in registerActivity
    CountingIdlingResource mIdlingResource;

    @Before
    public void setUp() {
        Intents.init();
    }

    @Before
    public void registerIdlingResource() {
        registerScenario.getScenario().onActivity(activity ->
                mIdlingResource = activity.getIdlingResource()
        );
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void registerWithEmptyUsernameGetFocus() {
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithTooShortUsernameGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithTooLongUsernameGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("toolongusernametobeprocessed"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithEmptyEmailGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerEmailEt)).check(matches(hasFocus()));

    }

    @Test
    public void registerWithEmptyPasswordGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerPasswordEt)).check(matches(hasFocus()));
    }

    @Test
    public void registerUserFiresLoginIntentCorrectly() {
        String userTestUsername = "usernameTest";
        String userTestEmail = "email.test@epfl.ch";
        String userTestPassword = "Password1";

        deleteUser(userTestEmail, userTestPassword);

        // Test to create the user
        onView(withId(R.id.registerUsernameEt)).perform(typeText(userTestUsername), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText(userTestEmail), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText(userTestPassword), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        intended(toPackage("com.sdp.swiftwallet"));
    }

    // Used by registerUserFiresLoginIntentCorrectly test
    private void deleteUser(String userTestEmail, String userTestPassword) {
        // SignIn user to test and delete it if it exists
        mIdlingResource.increment();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInWithEmailAndPassword(userTestEmail, userTestPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.delete().addOnCompleteListener(task1 ->
                                    mIdlingResource.decrement()
                    );
                } else { mIdlingResource.decrement(); }
            });
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            user.delete().addOnCompleteListener(task -> mIdlingResource.decrement());
        }
    }

    @Test
    public void registerUserFailsCorrectly() {
        String userTestUsername = "usernameTest";
        String userTestEmail = "email.test@epfl.ch";
        String userTestPassword = "Password1";

        // Create user before creating it for test so that it fails
        createUser(userTestEmail, userTestPassword);

        // Test to create the user
        onView(withId(R.id.registerUsernameEt)).perform(typeText(userTestUsername), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText(userTestEmail), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText(userTestPassword), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerBtn)).check(matches(isDisplayed()));
    }

    // Used by registerUserFailsCorrectly test
    private void createUser(String userTestEmail, String userTestPassword) {
        mIdlingResource.increment();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userTestEmail, userTestPassword)
                .addOnCompleteListener(task -> mIdlingResource.decrement());
    }

    @Test
    public void registerWithWrongEmailGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("password.t"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("michel"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerEmailEt)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithWrongPasswordGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("url"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("michel@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerPasswordEt)).check(matches(hasFocus()));

    }

    @Test
    public void registerWithWrongUsernameGetFocus() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("*"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("Aliska74!"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("michel@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void registerBackButtonFiresIntentCorrectly(){
        onView(withId(R.id.goBackRegister)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
    }




}