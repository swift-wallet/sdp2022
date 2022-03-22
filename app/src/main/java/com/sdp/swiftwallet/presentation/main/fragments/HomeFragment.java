package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.presentation.qrcode.QRActivity;
import com.sdp.swiftwallet.WalletActivity;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.main.wallets.fragments.WalletItemFragment;

/**
 * Home screen
 */
public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Assign proper methods upon creating view (for buttons)
     */
    private WalletItemFragment walletItemFragment;
    private Wallets wallets;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
  
    /**
     * start QR activity
     */
    public void startQR(){
        Intent intent = new Intent(getActivity(), QRActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the wallet
     */
    public void openWallet() {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the crypto values
     */
    public void startCryptoValues(){
        Intent intent = new Intent(getActivity(), CryptoValuesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        walletItemFragment = new WalletItemFragment();
        wallets = new Wallets(SeedGenerator.generateSeed());
        getChildFragmentManager().beginTransaction().replace(R.id.home_nested_frag_container, walletItemFragment).commit();
        requireActivity().findViewById(R.id.create_address_button).setOnClickListener((v) -> createAddress());
    }

    private void createAddress(){
        int walletID = wallets.generateWallet();
        walletItemFragment.addWalletItem(wallets.getWalletFromId(walletID));
    }


}