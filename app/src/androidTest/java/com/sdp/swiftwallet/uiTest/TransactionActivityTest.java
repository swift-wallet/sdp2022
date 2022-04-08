package com.sdp.swiftwallet.uiTest;

/**
@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class TransactionActivityTest {


    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Currency> currencyList = new ArrayList<>();

    static {
        currencyList.add(CURR_1);
        currencyList.add(CURR_2);
        currencyList.add(CURR_3);
        currencyList.add(CURR_4);
    }

    private Intent i;
    private DummyHistoryProducer producer;

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Before
    public void setupIntent() {
        hiltRule.inject();
        i = new Intent(ApplicationProvider.getApplicationContext(), TransactionActivity.class);
        producer = new DummyHistoryProducer();
        ((SwiftWalletApp) ApplicationProvider
                .getApplicationContext())
                .setTransactionHistoryProducer(producer);
    }

    @Test
    public void historyButtonIsDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void statsButtonIsDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void fragmentLayoutIsDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_flFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void activityStartsWithHistoryFragmentDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void historyButtonDisplaysHistoryFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyButton)).perform(click());
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void statsButtonDisplaysStatsFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void canSwitchBackToHistoryFragmentAfterGoingToStats() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            onView(withId(R.id.transaction_historyButton)).perform(click());
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void recyclerViewIsDisplayedInHistoryFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyButton)).perform(click());
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.transaction_recyclerView)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void pieChartIsDisplayedInStatsFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.transaction_pieChart)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addingANewTransactionDisplaysItInHistory() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            Transaction t1 = new Transaction(2.2, CURR_2, MY_WALL, THEIR_WALL, 1);
            Transaction t2 = new Transaction(17.5, CURR_1, MY_WALL, THEIR_WALL, 2);
            Transaction t3 = new Transaction(-19, CURR_3, MY_WALL, THEIR_WALL, 3);
            Transaction t4 = new Transaction(22, CURR_4, MY_WALL, THEIR_WALL, 4);
            Transaction t5 = new Transaction(76, CURR_2, MY_WALL, THEIR_WALL, 5);
            producer.addTransaction(t1);
            producer.addTransaction(t2);
            producer.addTransaction(t3);
            producer.addTransaction(t4);
            producer.addTransaction(t5);
            producer.alertAll();
        }
    }

    @Test
    public void addingANewTransactionDisplaysItInStats() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            Transaction t1 = new Transaction(2.2, CURR_2, MY_WALL, THEIR_WALL, 1);
            Transaction t2 = new Transaction(17.5, CURR_1, MY_WALL, THEIR_WALL, 2);
            Transaction t3 = new Transaction(-19, CURR_3, MY_WALL, THEIR_WALL, 3);
            Transaction t4 = new Transaction(22, CURR_4, MY_WALL, THEIR_WALL, 4);
            Transaction t5 = new Transaction(76, CURR_2, MY_WALL, THEIR_WALL, 5);
            producer.addTransaction(t1);
            producer.addTransaction(t2);
            producer.addTransaction(t3);
            producer.addTransaction(t4);
            producer.addTransaction(t5);
            producer.alertAll();
        }
    }

    public static class DummyHistoryProducer implements TransactionHistoryProducer {
        private final List<TransactionHistorySubscriber> subscribers = new ArrayList<>();
        private final List<Transaction> transactions = new ArrayList<>();

        @Override
        public boolean subscribe(TransactionHistorySubscriber subscriber) {
            subscriber.receiveTransactions(transactions);
            return subscribers.add(subscriber);
        }

        @Override
        public boolean unsubscribe(TransactionHistorySubscriber subscriber) {
            if (subscribers.contains(subscriber)) {
                return subscribers.remove(subscriber);
            }
            return true;
        }

        public void alertAll() {
            for (TransactionHistorySubscriber subscriber : subscribers) {
                subscriber.receiveTransactions(transactions);
            }
        }

        public void addTransaction(Transaction t) {
            transactions.add(t);
        }
    }
}**/
