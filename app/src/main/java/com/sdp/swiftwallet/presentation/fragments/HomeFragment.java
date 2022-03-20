package com.sdp.swiftwallet.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenai.jffi.Main;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.MainActivity;
import com.sdp.swiftwallet.QRActivity;
import com.sdp.swiftwallet.WalletActivity;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.fragments.wallets.WalletItemFragment;

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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        if( Wallets.hasSeed( mainActivity ) ){
            wallets = Wallets.recoverWallets( mainActivity );
            View seedNotSetup = mainActivity.findViewById(R.id.seed_not_setup);
            View seedSetup = mainActivity.findViewById(R.id.seed_setup);
            ViewGroup viewGroup = ((ViewGroup) seedSetup.getParent());
            viewGroup.removeView(seedNotSetup);
            viewGroup.removeView(seedSetup);
        } else {
            mainActivity.findViewById(R.id.seed_setup).setOnClickListener((v) -> goToSeedSetup());
            mainActivity.findViewById(R.id.create_address_button).setVisibility(View.INVISIBLE);
        }
        //wallets = new Wallets(SeedGenerator.generateSeed());
        //walletItemFragment = new WalletItemFragment();
        //getChildFragmentManager().beginTransaction().replace(R.id.home_nested_frag_container, walletItemFragment).commit();
        //requireActivity().findViewById(R.id.create_address_button).setOnClickListener((v) -> createAddress());
    }

    private void createAddress(){
        int walletID = wallets.generateWallet();
        walletItemFragment.addWalletItem(wallets.getWalletFromId(walletID));
    }
    private void goToSeedSetup(){

    }


}