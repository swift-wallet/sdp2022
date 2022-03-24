package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.TransactionHistoryActivity;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionHistoryGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class TransactionActivityTest {
    private final static Currency CURR = new Currency("DumbCoin", "DUM", 9.5);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Transaction> list = new ArrayList<>();

    static {
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            double amount = -100 + r.nextDouble() * 200;
            Transaction t = new Transaction(amount, CURR, MY_WALL, THEIR_WALL, i);
            list.add(t);
        }
    }


    @Test
    public void test() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), TransactionHistoryActivity.class);
        intent.putExtra(
                ApplicationProvider.getApplicationContext().getString(R.string.transactionHistoryGeneratorExtraKey),
                (Parcelable) new DummyTransactionGenerator()
        );

        try (ActivityScenario<TransactionHistoryActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.transactionsRecyclerView)).check(matches(isDisplayed()));
        }
    }

    public static class DummyTransactionGenerator implements TransactionHistoryGenerator, Parcelable {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        @Override
        public List<Transaction> getTransactionHistory() {
            return list;
        }

        public static final Parcelable.Creator<DummyTransactionGenerator> CREATOR = new Parcelable.Creator<DummyTransactionGenerator>() {
            @Override
            public DummyTransactionGenerator createFromParcel(Parcel source) {
                return new DummyTransactionGenerator(source);
            }

            @Override
            public DummyTransactionGenerator[] newArray(int size) {
                return new DummyTransactionGenerator[size];
            }
        };

        private DummyTransactionGenerator(Parcel in) {
        }

        public DummyTransactionGenerator() {
        }
    }
}
