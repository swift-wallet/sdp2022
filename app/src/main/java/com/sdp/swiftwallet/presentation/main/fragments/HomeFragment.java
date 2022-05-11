package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.di.wallet.WalletProvider;
import com.sdp.swiftwallet.domain.model.object.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.WalletInfoActivity;
import com.sdp.swiftwallet.presentation.wallet.WalletSelectActivity;

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

    private boolean hasSeed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasSeed = walletProvider.hasWallets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentView.findViewById(R.id.seed_setup).setOnClickListener(this::launchSeedSetup);
        fragmentView.findViewById(R.id.first_wallet_setup_button).setOnClickListener(this::launchWalletSelector);
        fragmentView.findViewById(R.id.item_address).setOnClickListener(this::launchWalletSelector);
        fragmentView.findViewById(R.id.show_qr_button).setOnClickListener(this::launchWalletInfo);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkSeedState();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkSeedState();
    }

    private void checkSeedState() {
        if( walletProvider.hasWallets() != hasSeed ){
            hasSeed = !hasSeed;
        }
        if(hasSeed) {
            recoverWalletView();
            fragmentView.findViewById(R.id.seed_setup_layout).setVisibility(View.GONE);
        } else {
            fragmentView.findViewById(R.id.seed_setup_layout).setVisibility(View.VISIBLE);
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
        walletProvider.getWallets().setCurrentKeyPair(walletKeyPair);
    }

    // Resetting the view to handle wallets list
    private void recoverWalletView() {
        IWalletKeyPair currentKeyPair = walletProvider.getWallets().getCurrentKeyPair();
        if(currentKeyPair != null){
            setSelectedWallet(currentKeyPair);
            fragmentView.findViewById(R.id.wallet_container).setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.first_wallet_setup_layout).setVisibility(View.GONE);
        } else {
            fragmentView.findViewById(R.id.first_wallet_setup_layout).setVisibility(View.VISIBLE);
        }
    }

    private void launchSeedSetup(View v) {
        Intent intent = new Intent(requireActivity(), CreateSeedActivity.class);
        startActivity(intent);
    }

    private void launchWalletInfo(View v) {
        Intent intent = new Intent(requireActivity(), WalletInfoActivity.class);
        IWalletKeyPair walletKeyPair = walletProvider.getWallets().getCurrentKeyPair(); // check if not null
        intent.putExtra(WalletInfoActivity.ADDRESS_EXTRA, walletKeyPair.getHexPublicKey());
        intent.putExtra(WalletInfoActivity.BALANCE_EXTRA, walletKeyPair.getNativeBalance().toString());
        startActivity(intent);
    }

    private void launchWalletSelector(View v) {
        Intent selectorIntent = new Intent(requireContext(), WalletSelectActivity.class);
        // Maybe add extra of already selected wallet later ?
        startActivity(selectorIntent);
    }
}