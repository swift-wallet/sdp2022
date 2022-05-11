package com.sdp.swiftwallet.di;

import android.content.Context;

import com.sdp.swiftwallet.domain.model.CurrencyBank;
import com.sdp.swiftwallet.domain.model.RequestQueueCurrencyPriceChecker;
import com.sdp.swiftwallet.domain.model.SwiftWalletCurrencyBank;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt Module to inject CurrencyBankModule
 */
@Module
@InstallIn(SingletonComponent.class)
public class CurrencyBankModule {

    /**
     * @return provides a currency bank
     */
    @Provides
    CurrencyBank provideCurrencyBank(CurrencyBank.PriceChecker priceChecker) {
        return new SwiftWalletCurrencyBank(priceChecker);
    }

    /**
     * @return provides a price checker
     */
    @Provides
    CurrencyBank.PriceChecker provideRequestQueuePriceChecker(@ApplicationContext Context context) {
        return new RequestQueueCurrencyPriceChecker(context);
    }
}
