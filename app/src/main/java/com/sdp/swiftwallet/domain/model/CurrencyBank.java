package com.sdp.swiftwallet.domain.model;

import java.util.Map;

public interface CurrencyBank {
    Currency getCurrency(String symbol);

    interface PriceChecker {
        void fill(Map<String, Currency> currencyMap);
    }
}
