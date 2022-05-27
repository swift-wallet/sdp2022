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
import java.util.Optional;
import java.util.Random;

@RunWith(JUnit4.class)
public class TransactionTest {
    private final static Currency CURR = new Currency("Dummy", "DUM", 9.5);
    private final static Date DUMMY_DATE = new Date();
    private final static String SENDER_WALL = "SENDER_WALL";
    private final static String RECEIVER_WALL = "RECEIVER_WALL";
    private final static String SENDER_ID = "SENDER_ID";
    private final static String RECEIVER_ID = "RECEIVER_ID";

    private final static double MAX_AMOUNT = 10000;
    private final static int ITERATIONS = 100;

    @Test
    public void constructorThrowsIAEOnNullCurr() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        null,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.empty(),
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullDate() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        null,
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.empty(),
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullSenderWallet() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        DUMMY_DATE,
                        null,
                        RECEIVER_WALL,
                        Optional.empty(),
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullReceiverWallet() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        null,
                        Optional.empty(),
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullSenderID() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        RECEIVER_WALL,
                        null,
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullReceiverID() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.empty(),
                        null));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnIdenticalWallets() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        SENDER_WALL,
                        Optional.empty(),
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Sender and receiver wallets of transaction cannot be the same"));
    }

    @Test
    public void constructorWorksWithSenderIDAndNoReceiverID() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.of(SENDER_ID),
                Optional.empty()
        );
    }

    @Test
    public void constructorWorksWithReceiverIDAndNoSenderID() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.of(RECEIVER_ID)
        );
    }

    @Test
    public void constructorThrowsIAEOnIdenticalUserIDs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        0,
                        CURR,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.of(SENDER_ID),
                        Optional.of(SENDER_ID)));
        assertThat(ex.getMessage(), is("Sender and receiver IDs of transaction cannot be the same"));
    }

    @Test
    public void constructorThrowsIAEOnNegativeAmount() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        -1,
                        CURR,
                        0,
                        DUMMY_DATE,
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.empty(),
                        Optional.empty()));
        assertThat(ex.getMessage(), is("Amount of transaction must be positive"));
    }

    @Test
    public void constructWorksWithProperArguments() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.of(SENDER_ID),
                Optional.of(RECEIVER_ID)
        );
    }

    @Test
    public void getAmountReturnsCorrectAmount() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            double amount = MAX_AMOUNT * r.nextDouble();
            Transaction t = new Transaction(
                    amount,
                    CURR,
                    0,
                    DUMMY_DATE,
                    SENDER_WALL,
                    RECEIVER_WALL,
                    Optional.empty(),
                    Optional.empty()
            );
            assertThat(t.getAmount(), is(amount));
        }
    }

    @Test
    public void getCurrencyReturnsCorrectCurr() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );
        assertThat(t.getCurr().getSymbol(), is(CURR.getSymbol()));
        assertThat(t.getCurr().getName(), is(CURR.getName()));
        assertThat(t.getCurr().getValue(), is(CURR.getValue()));
    }

    @Test
    public void getConvertedAmountReturnsCorrectAmount() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            double amount = MAX_AMOUNT * r.nextDouble();
            double convertedAmount = amount * CURR.getValue();
            Transaction t = new Transaction(
                    amount,
                    CURR,
                    0,
                    DUMMY_DATE,
                    SENDER_WALL,
                    RECEIVER_WALL,
                    Optional.empty(),
                    Optional.empty()
            );
            assertThat(t.getConvertedAmount(), is(convertedAmount));
        }
    }

    @Test
    public void getTransactionIDReturnsCorrectID() {
        Random r = new Random();
        for (int i = 0; i < ITERATIONS; i++) {
            int id = r.nextInt();
            Transaction t = new Transaction(
                    0,
                    CURR,
                    id,
                    DUMMY_DATE,
                    SENDER_WALL,
                    RECEIVER_WALL,
                    Optional.empty(),
                    Optional.empty()
            );
            assertThat(t.getTransactionID(), is(id));
        }
    }

    @Test
    public void getDateReturnsCorrectDate() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );
        assertThat(t.getDate(), is(DUMMY_DATE));
    }

    @Test
    public void getSenderWalletIDReturnsCorrectID() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );
        assertThat(t.getSenderWalletID(), is(SENDER_WALL));
    }

    @Test
    public void getReceiverWalletIDReturnsCorrectID() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );
        assertThat(t.getReceiverWalletID(), is(RECEIVER_WALL));
    }

    @Test
    public void getSenderIDReturnsCorrectID() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );
        assertThat(t.getSenderID().isPresent(), is(false));

        t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.of(SENDER_ID),
                Optional.empty()
        );
        assertThat(t.getSenderID().isPresent(), is(true));
        assertThat(t.getSenderID().get(), is(SENDER_ID));
    }

    @Test
    public void getReceiverIDReturnsCorrectID() {
        Transaction t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );
        assertThat(t.getReceiverID().isPresent(), is(false));

        t = new Transaction(
                0,
                CURR,
                0,
                DUMMY_DATE,
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.of(RECEIVER_ID)
        );
        assertThat(t.getReceiverID().isPresent(), is(true));
        assertThat(t.getReceiverID().get(), is(RECEIVER_ID));
    }
}
