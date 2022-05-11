package com.sdp.swiftwallet.domain.object.bank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import com.sdp.swiftwallet.domain.model.object.currency.Currency;
import com.sdp.swiftwallet.domain.model.bank.CurrencyBank;
import com.sdp.swiftwallet.domain.model.bank.SwiftWalletCurrencyBank;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class SwiftWalletCurrencyBankTest {
    private final static List<Currency> dummyCurrencies = new ArrayList<>();

    static {
        dummyCurrencies.add(new Currency("Dumbcoin", "DUM", 12));
        dummyCurrencies.add(new Currency("Bitcoin", "BTC", 14));
        dummyCurrencies.add(new Currency("Swiftcoin", "SWT", 10));
    }

    private DummyPriceChecker priceChecker;

    @Before
    public void initPriceChecker() {
        priceChecker = new DummyPriceChecker();
    }

    @Test
    public void getCurrencyReturnsNullIfNotInMap() {
        priceChecker.setFillWithData(false);

        SwiftWalletCurrencyBank bank = new SwiftWalletCurrencyBank(priceChecker);
        for (Currency curr : dummyCurrencies) {
            Currency returnedCurr = bank.getCurrency(curr.getSymbol());
            assertThat(returnedCurr, is(nullValue()));
        }
    }

    @Test
    public void getCurrencyThrowsExceptionOnNullSymbol() {
        SwiftWalletCurrencyBank bank = new SwiftWalletCurrencyBank(priceChecker);
        for (Currency curr : dummyCurrencies) {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> bank.getCurrency(null)
            );

            assertThat(exception.getMessage(), is("Null symbol"));
        }
    }

    @Test
    public void getCurrencyReturnsSameCurrencyIfInMap() {
        SwiftWalletCurrencyBank bank = new SwiftWalletCurrencyBank(priceChecker);

        for (Currency curr : dummyCurrencies) {
            Currency returnedCurr = bank.getCurrency(curr.getSymbol());
            assertThat(returnedCurr, is(curr));
        }
    }

    public static class DummyPriceChecker implements CurrencyBank.PriceChecker {

        private boolean fillWithData;

        public DummyPriceChecker() {
            fillWithData = true;
        }

        public void setFillWithData(boolean b) {
            fillWithData = b;
        }

        @Override
        public void fill(Map<String, Currency> currencyMap) {
            if (fillWithData) {
                for (Currency curr : dummyCurrencies) {
                    currencyMap.put(curr.getSymbol(), curr);
                }
            }
        }
    }
}
