package com.sdp.swiftwallet.domain.model;

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
        return currencyMap.get(symbol);
    }
}
