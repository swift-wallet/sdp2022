package com.sdp.swiftwallet.presentation.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.di.WalletProvider;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for wallect selection
 */
@AndroidEntryPoint
public class WalletSelectActivity extends AppCompatActivity {

    private WalletItemFragment walletItemFragment;

    @Inject
    public WalletProvider walletProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_select);
        walletItemFragment = new WalletItemFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.select_nested_frag_container, walletItemFragment, WalletItemFragment.class.getName())
                .setReorderingAllowed(true)
                .commit();
        recoverWalletsList();
        findViewById(R.id.create_address_button).setOnClickListener(this::createAddress);
    }

    /**
     * Recover a list of wallets
     */
    private void recoverWalletsList(){
        int counter = walletProvider.getWallets().getCounter();
        for(int i=0 ; i< counter; i++){
            walletItemFragment.addWalletItem(walletProvider.getWallets().getWalletFromId(i));
        }
    }

    /**
     * Create an address on view v
     * @param v view
     */
    private void createAddress(View v){
        int walletID = walletProvider.getWallets().generateWallet();
        walletItemFragment.addWalletItem(walletProvider.getWallets().getWalletFromId(walletID));
    }
}