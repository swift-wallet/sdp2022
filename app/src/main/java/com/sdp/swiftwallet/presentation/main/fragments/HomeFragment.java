package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

import java.util.Objects;

/**
 * Home screen
 */
public class HomeFragment extends Fragment {

    private View fragmentView;
    private WalletItemFragment walletItemFragment;
    private Wallets wallets;
    private boolean hasSeed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walletItemFragment = new WalletItemFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_nested_frag_container, walletItemFragment, WalletItemFragment.class.getName())
                .setReorderingAllowed(true)
                .commit();

        hasSeed = SeedGenerator.hasSeed(requireActivity());
        if( hasSeed ){
            wallets = SeedGenerator.recoverWallets( requireActivity() );
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
        if( !hasSeed ){
            fragmentView.findViewById(R.id.seed_setup).setOnClickListener((v) -> goToSeedSetup());
            fragmentView.findViewById(R.id.create_address_button).setVisibility(View.GONE);
        } else {
            recoverWalletsView();
            recoverWalletsList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if( hasSeed != SeedGenerator.hasSeed(requireActivity()) ){
            if( !hasSeed ){
                wallets = SeedGenerator.recoverWallets(requireActivity());
                recoverWalletsView();
                recoverWalletsList();
                hasSeed = true;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( !Objects.isNull(wallets) ){
            wallets.saveCounter(requireActivity());
        }
    }

    // Resetting the view to handle wallets list
    private void recoverWalletsView() {
        View seedNotSetup = fragmentView.findViewById(R.id.seed_not_setup);
        View seedSetup = fragmentView.findViewById(R.id.seed_setup);
        ViewGroup viewGroup = ((ViewGroup) seedSetup.getParent());
        viewGroup.removeView(seedNotSetup);
        viewGroup.removeView(seedSetup);
        fragmentView.findViewById(R.id.create_address_button).setVisibility(View.VISIBLE);
        fragmentView.findViewById(R.id.create_address_button).setOnClickListener((v) -> createAddress());
    }

    private void recoverWalletsList(){
        int counter = wallets.getCounter();
        for(int i=0 ; i< counter; i++){
            walletItemFragment.addWalletItem(wallets.getWalletFromId(i));
        }
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