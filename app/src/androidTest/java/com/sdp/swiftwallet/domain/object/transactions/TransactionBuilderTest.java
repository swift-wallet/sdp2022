package com.sdp.swiftwallet.domain.object.transactions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.Random;

@RunWith(JUnit4.class)
public class TransactionBuilderTest {
    private final static Currency CURR = new Currency("Dummy", "DUM", 9.5);
    private final static Date DUMMY_DATE = new Date();
    private final static String SENDER_WALL = "SENDER_WALL";
    private final static String RECEIVER_WALL = "RECEIVER_WALL";
    private final static String SENDER_ID = "SENDER_ID";
    private final static String RECEIVER_ID = "RECEIVER_ID";

    private final static double MAX_AMOUNT = 10000;
    private final static int ITERATIONS = 100;

    @Test
    public void setAmountSetsProperAmount() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            double amount = MAX_AMOUNT * r.nextDouble();
            Transaction.Builder builder = new Transaction.Builder();
            builder.setAmount(amount);
            builder.setCurrency(CURR)
                    .setTransactionID(0)
                    .setDate(DUMMY_DATE)
                    .setSenderWalletID(SENDER_WALL)
                    .setReceiverWalletID(RECEIVER_WALL)
                    .setSenderID(SENDER_ID)
                    .setReceiverID(RECEIVER_ID);
            Transaction t = builder.build();

            assertThat(t.getAmount(), is(amount));
        }
    }

    @Test
    public void setCurrencySetsProperCurr() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setCurrency(CURR);
        builder.setAmount(0)
                .setTransactionID(0)
                .setDate(DUMMY_DATE)
                .setSenderWalletID(SENDER_WALL)
                .setReceiverWalletID(RECEIVER_WALL)
                .setSenderID(SENDER_ID)
                .setReceiverID(RECEIVER_ID);
        Transaction t = builder.build();

        assertThat(t.getCurr().getValue(), is(CURR.getValue()));
        assertThat(t.getCurr().getSymbol(), is(CURR.getSymbol()));
        assertThat(t.getCurr().getName(), is(CURR.getName()));
    }

    @Test
    public void setAmountAndCurrencySetsProperAmountAndCurr() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            double amount = MAX_AMOUNT * r.nextDouble();
            Transaction.Builder builder = new Transaction.Builder();
            builder.setAmountAndCurrency(amount, CURR);
            builder.setTransactionID(0)
                    .setDate(DUMMY_DATE)
                    .setSenderWalletID(SENDER_WALL)
                    .setReceiverWalletID(RECEIVER_WALL)
                    .setSenderID(SENDER_ID)
                    .setReceiverID(RECEIVER_ID);
            Transaction t = builder.build();

            assertThat(t.getAmount(), is(amount));
            assertThat(t.getCurr().getValue(), is(CURR.getValue()));
            assertThat(t.getCurr().getSymbol(), is(CURR.getSymbol()));
            assertThat(t.getCurr().getName(), is(CURR.getName()));
        }
    }

    @Test
    public void setTransactionIDSetsProperID() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            int id = r.nextInt();
            Transaction.Builder builder = new Transaction.Builder();
            builder.setTransactionID(id);
            builder.setAmount(0)
                    .setCurrency(CURR)
                    .setDate(DUMMY_DATE)
                    .setSenderWalletID(SENDER_WALL)
                    .setReceiverWalletID(RECEIVER_WALL)
                    .setSenderID(SENDER_ID)
                    .setReceiverID(RECEIVER_ID);
            Transaction t = builder.build();

            assertThat(t.getTransactionID(), is(id));
        }
    }

    @Test
    public void setDateSetsProperDate() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setDate(DUMMY_DATE);
        builder.setAmount(0)
                .setCurrency(CURR)
                .setTransactionID(0)
                .setSenderWalletID(SENDER_WALL)
                .setReceiverWalletID(RECEIVER_WALL)
                .setSenderID(SENDER_ID)
                .setReceiverID(RECEIVER_ID);
        Transaction t = builder.build();

        assertThat(t.getDate(), is(DUMMY_DATE));
    }

    @Test
    public void setMetadataSetsProperMetadata() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            int id = r.nextInt();
            Transaction.Builder builder = new Transaction.Builder();
            builder.setMetadata(id, DUMMY_DATE);
            builder.setAmount(0)
                    .setCurrency(CURR)
                    .setSenderWalletID(SENDER_WALL)
                    .setReceiverWalletID(RECEIVER_WALL)
                    .setSenderID(SENDER_ID)
                    .setReceiverID(RECEIVER_ID);
            Transaction t = builder.build();

            assertThat(t.getTransactionID(), is(id));
            assertThat(t.getDate(), is(DUMMY_DATE));
        }
    }

    @Test
    public void setSenderWalletIDSetsProperID() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setSenderWalletID(SENDER_WALL);
        builder.setAmount(0)
                .setCurrency(CURR)
                .setTransactionID(0)
                .setDate(DUMMY_DATE)
                .setReceiverWalletID(RECEIVER_WALL)
                .setSenderID(SENDER_ID)
                .setReceiverID(RECEIVER_ID);
        Transaction t = builder.build();

        assertThat(t.getSenderWalletID(), is(SENDER_WALL));
    }

    @Test
    public void setReceiverWalletIDSetsProperID() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setReceiverWalletID(RECEIVER_WALL);
        builder.setAmount(0)
                .setCurrency(CURR)
                .setTransactionID(0)
                .setDate(DUMMY_DATE)
                .setSenderWalletID(SENDER_WALL)
                .setSenderID(SENDER_ID)
                .setReceiverID(RECEIVER_ID);
        Transaction t = builder.build();

        assertThat(t.getReceiverWalletID(), is(RECEIVER_WALL));
    }

    @Test
    public void setWalletIDsSetsProperIDs() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);
        builder.setAmount(0)
                .setCurrency(CURR)
                .setTransactionID(0)
                .setDate(DUMMY_DATE)
                .setSenderID(SENDER_ID)
                .setReceiverID(RECEIVER_ID);
        Transaction t = builder.build();

        assertThat(t.getSenderWalletID(), is(SENDER_WALL));
        assertThat(t.getReceiverWalletID(), is(RECEIVER_WALL));
    }

    @Test
    public void setSenderIDSetsProperID() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setSenderID(SENDER_ID);
        builder.setAmount(0)
                .setCurrency(CURR)
                .setTransactionID(0)
                .setDate(DUMMY_DATE)
                .setSenderWalletID(SENDER_WALL)
                .setReceiverWalletID(RECEIVER_WALL)
                .setReceiverID(RECEIVER_ID);
        Transaction t = builder.build();

        assertThat(t.getSenderID().isPresent(), is(true));
        assertThat(t.getSenderID().get(), is(SENDER_ID));
    }

    @Test
    public void setReceiverIDSetsProperID() {
        Transaction.Builder builder = new Transaction.Builder();
        builder.setReceiverID(RECEIVER_ID);
        builder.setAmount(0)
                .setCurrency(CURR)
                .setTransactionID(0)
                .setDate(DUMMY_DATE)
                .setSenderWalletID(SENDER_WALL)
                .setReceiverWalletID(RECEIVER_WALL)
                .setSenderID(SENDER_ID);
        Transaction t = builder.build();

        assertThat(t.getReceiverID().isPresent(), is(true));
        assertThat(t.getReceiverID().get(), is(RECEIVER_ID));
    }
}
