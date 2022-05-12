package com.sdp.swiftwallet.domain.model.currency;

import com.sdp.swiftwallet.domain.model.currency.Currency;
import java.util.Map;

/**
 * CurrencyBank interface
 */
public interface CurrencyBank {

    /**
     * Getter for the Currency associated to a symbol
     *
     * @param symbol the symbol of the Currency
     * @return the Currency object associated to the Symbol symbol
     */
    Currency getCurrency(String symbol);

    /**
     * PriceChecker interface
     */
    interface PriceChecker {
        /**
         * Fill the currencyMap with Currency objects (eventually checking prices while filling it)
         *
         * @param currencyMap the Map to fill with Currency objects
         */
        void fill(Map<String, Currency> currencyMap);
    }
}
