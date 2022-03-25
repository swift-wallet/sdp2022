package com.sdp.swiftwallet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;

@RunWith(JUnit4.class)
public class TransactionTest {
    private final static Currency CURR = new Currency("Dummy", "DUM", 9.5);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";

    @Test
    public void constructorThrowsIAEOnNullCurr() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(0, null, MY_WALL, THEIR_WALL, 0));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullMyWallet() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(0, CURR, null, THEIR_WALL, 0));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnNullTheirWallet() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(0, CURR, MY_WALL, null, 0));
        assertThat(ex.getMessage(), is("Null arguments"));
    }

    @Test
    public void constructorThrowsIAEOnDuplicateWallet() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(0, CURR, MY_WALL, MY_WALL, 0));
        assertThat(ex.getMessage(), is("Wallets cannot be the same"));
    }

    @Test
    public void getAmountReturnsCorrectAmount() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            double amount = 1000 * r.nextDouble();
            Transaction t = new Transaction(amount, CURR, MY_WALL, THEIR_WALL, 0);
            assertThat(t.getAmount(), is(amount));
        }
    }

    @Test
    public void getConvertedAmountReturnsCorrectAmount() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            double amount = 1000 * r.nextDouble();
            double convertedAmount = amount * CURR.getValue();
            Transaction t = new Transaction(amount, CURR, MY_WALL, THEIR_WALL, 0);
            assertThat(t.getConvertedAmount(), is(convertedAmount));
        }
    }

    @Test
    public void getSymbolReturnsCorrectSymbol() {
        Transaction t = new Transaction(10, CURR, MY_WALL, THEIR_WALL, 0);
        assertThat(t.getSymbol(), is(CURR.getSymbol()));
    }

    @Test
    public void toStringReturnsCorrectStringOnNegativeAmount() {
        Transaction t = new Transaction(-15, CURR, MY_WALL, THEIR_WALL, 0);
        assertThat(t.toString(), is("15.0 DUM from your wallet MY_WALL to wallet THEIR_WALL"));
    }

    @Test
    public void toStringReturnsCorrectStringOnPositiveAmount() {
        Transaction t = new Transaction(15, CURR, MY_WALL, THEIR_WALL, 0);
        assertThat(t.toString(), is("15.0 DUM from wallet THEIR_WALL to your wallet MY_WALL"));
    }

    @Test
    public void getTransactionIDReturnsCorrectID() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int id = r.nextInt();
            Transaction t = new Transaction(0, CURR, MY_WALL, THEIR_WALL, id);
            assertThat(t.getTransactionID(), is(id));
        }
    }
}
