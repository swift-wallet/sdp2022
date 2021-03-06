package com.sdp.swiftwallet.domain.model.currency;

import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.currency.CurrencyBank;
import java.util.HashMap;
import java.util.Map;

/**
 * CurrencyBank implementation for SwiftWallet
 */
public class SwiftWalletCurrencyBank implements CurrencyBank {
    private final Map<String, Currency> currencyMap;

    public SwiftWalletCurrencyBank(CurrencyBank.PriceChecker priceChecker) {
        currencyMap = new HashMap<>();
        priceChecker.fill(currencyMap);
    }

    @Override
    public Currency getCurrency(String symbol) {
        if (symbol == null) {
            throw new NullPointerException("Null symbol");
        }
        return currencyMap.get(symbol);
    }
}
