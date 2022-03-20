package com.sdp.swiftwallet.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

/**
 * Home screen
 */
public class HomeFragment extends Fragment {

    private View fragmentView;
    private WalletItemFragment walletItemFragment;
    private Wallets wallets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( SeedGenerator.hasSeed(requireActivity()) ){
            wallets = SeedGenerator.recoverWallets( requireActivity() );
            walletItemFragment = new WalletItemFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentView = view;
        if( SeedGenerator.hasSeed(requireActivity()) ){
            recoverWalletsView();
        } else {
            fragmentView.findViewById(R.id.seed_setup).setOnClickListener((v) -> goToSeedSetup());
            fragmentView.findViewById(R.id.create_address_button).setVisibility(View.GONE);
        }
    }

    // Resetting the view to handle wallets list
    private void recoverWalletsView() {
        View seedNotSetup = fragmentView.findViewById(R.id.seed_not_setup);
        View seedSetup = fragmentView.findViewById(R.id.seed_setup);
        ViewGroup viewGroup = ((ViewGroup) seedSetup.getParent());
        viewGroup.removeView(seedNotSetup);
        viewGroup.removeView(seedSetup);
        fragmentView.findViewById(R.id.create_address_button).setOnClickListener((v) -> createAddress());
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.home_nested_frag_container, walletItemFragment)
                .addToBackStack(null)
                .commit();
    }

    private void createAddress(){
        int walletID = wallets.generateWallet();
        walletItemFragment.addWalletItem(wallets.getWalletFromId(walletID));
    }
    private void goToSeedSetup(){
        Intent intent = new Intent(requireActivity(), CreateSeedActivity.class);
        startActivity(intent);
    }


}