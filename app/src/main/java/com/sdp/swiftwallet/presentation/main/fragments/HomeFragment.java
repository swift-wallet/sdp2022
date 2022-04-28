package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.di.WalletProvider;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Home screen
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private View fragmentView;
    private WalletItemFragment walletItemFragment;

    @Inject
    public WalletProvider walletProvider;

    @Inject
    public IWeb3Requests web3Requests;

    private boolean hasSeed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walletItemFragment = new WalletItemFragment();
        walletItemFragment.initWeb3(web3Requests);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_nested_frag_container, walletItemFragment, WalletItemFragment.class.getName())
                .setReorderingAllowed(true)
                .commit();

        hasSeed = walletProvider.hasWallets();
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
            // If context does not have a seed defined, show the buttons to be able to set it up
            fragmentView.findViewById(R.id.seed_setup).setOnClickListener((v) -> goToSeedSetup());
            fragmentView.findViewById(R.id.create_address_button).setVisibility(View.GONE);
        } else {
            // Else recover the wallets and update the UI
            recoverWalletsView();
            if(walletItemFragment.itemCount() != walletProvider.getWallets().getCounter()){
                recoverWalletsList();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if( walletProvider.hasWallets() != hasSeed ){
            // State might have changed when resumed, so seed might have been set or unset
            if( !hasSeed ){
                recoverWalletsView();
                recoverWalletsList();
                hasSeed = true;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if ( walletProvider.hasWallets() ){
            walletProvider.getWallets().saveCounter(requireActivity());
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
        int counter = walletProvider.getWallets().getCounter();
        for(int i=0 ; i< counter; i++){
            walletItemFragment.addWalletItem(walletProvider.getWallets().getWalletFromId(i));
        }
    }

    private void createAddress(){
        int walletID = walletProvider.getWallets().generateWallet();
        walletItemFragment.addWalletItem(walletProvider.getWallets().getWalletFromId(walletID));
    }
    private void goToSeedSetup(){
        Intent intent = new Intent(requireActivity(), CreateSeedActivity.class);
        startActivity(intent);
    }
}