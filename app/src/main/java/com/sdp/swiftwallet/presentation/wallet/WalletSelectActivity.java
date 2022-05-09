package com.sdp.swiftwallet.presentation.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.di.WalletProvider;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * The activity to be able to select a wallet among the created/imported ones
 */
@AndroidEntryPoint
public class WalletSelectActivity extends AppCompatActivity {

    private WalletItemFragment walletItemFragment;
    private View importWalletLayout;
    private EditText importInput;

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

        // Listener setting
        findViewById(R.id.create_address_button).setOnClickListener(this::createAddress);
        findViewById(R.id.show_import_address_button).setOnClickListener(this::showImportAddress);
        findViewById(R.id.import_address_button).setOnClickListener(this::importAddress);

        // Class variables defining
        importInput = findViewById(R.id.import_wallet_input);
        importWalletLayout = findViewById(R.id.import_wallet_layout);
    }

    private void recoverWalletsList(){
        int counter = walletProvider.getWallets().getCounter();
        for(int i=0 ; i < counter; i++){
            walletItemFragment.addWalletItem(walletProvider.getWallets().getWalletFromId(i));
        }
    }

    private void createAddress(View v){
        walletItemFragment.addWalletItem(walletProvider.getWallets().generateWallet());
    }

    private void importAddress(View v){
        String pk = importInput.getText().toString();
        IWalletKeyPair walletKeyPair = walletProvider.getWallets().importKeyPair(this, pk);
        walletItemFragment.addWalletItem(walletKeyPair);
        walletProvider.getWallets().setCurrentKeyPair(walletKeyPair);
        finish();
    }

    private void showImportAddress(View v){
        int visibility = importWalletLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
        importWalletLayout.setVisibility(visibility);
    }
}