package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.rpc.Help;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.di.WalletProvider;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.WalletSelectActivity;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Home screen
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private View fragmentView;

    @Inject
    public WalletProvider walletProvider;

    @Inject
    public IWeb3Requests web3Requests;

    private IWalletKeyPair selectedKeyPair = null;

    private boolean hasSeed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            fragmentView.findViewById(R.id.wallet_container).setVisibility(View.GONE);
        } else {
            // Else remove the seed setup buttons
            View seedNotSetup = fragmentView.findViewById(R.id.seed_not_setup);
            View seedSetup = fragmentView.findViewById(R.id.seed_setup);
            ViewGroup viewGroup = ((ViewGroup) seedSetup.getParent());
            viewGroup.removeView(seedNotSetup);
            viewGroup.removeView(seedSetup);

            // And recover wallet view if there is an existing wallet
            if(walletProvider.getWallets().getCounter() > 0){
                setSelectedWallet(walletProvider.getWallets().getWalletFromId(0));
                recoverWalletView();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if( walletProvider.hasWallets() != hasSeed ){
            // State might have changed when resumed, so seed might have been set or unset
            if( !hasSeed ){
                recoverWalletView();
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

    private void setSelectedWallet(IWalletKeyPair walletKeyPair) {
        ((TextView)fragmentView.findViewById(R.id.item_address))
                .setText(HelperFunctions.toShortenedFormatAddress(walletKeyPair.getHexPublicKey()));
        // Should calculate total worth later
        ((TextView)fragmentView.findViewById(R.id.ether_balance))
                .setText(walletKeyPair.getNativeBalance().toString());
        selectedKeyPair = walletKeyPair;
    }

    // Resetting the view to handle wallets list
    private void recoverWalletView() {
        fragmentView.findViewById(R.id.wallet_container).setVisibility(View.VISIBLE);
        fragmentView.findViewById(R.id.item_address).setOnClickListener(this::launchWalletSelector);
    }

    private void goToSeedSetup(){
        Intent intent = new Intent(requireActivity(), CreateSeedActivity.class);
        startActivity(intent);
    }

    private void launchWalletSelector(View v) {
        Intent selectorIntent = new Intent(requireContext(), WalletSelectActivity.class);
        // Maybe add extra of already selected wallet later ?
        startActivity(selectorIntent);
    }
}