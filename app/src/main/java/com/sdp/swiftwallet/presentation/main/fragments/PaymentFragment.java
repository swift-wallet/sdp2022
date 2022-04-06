package com.sdp.swiftwallet.presentation.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;

import java.util.Arrays;

/**
 * Payment fragment to send some cryptocurrency assets
 */
public class PaymentFragment extends Fragment {
    private SwiftWalletApp application;
    private Spinner walletsSpinner;
    private ArrayAdapter<String> arrayAdapter;
    private String[] walletsList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (SwiftWalletApp) requireActivity().getApplication();
        walletsList = addressesFromIWalletsKeyPair(application.getWallets().getWallets());
        arrayAdapter = new ArrayAdapter<String>(requireActivity(), R.layout.address_picker, walletsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        walletsSpinner = view.findViewById(R.id.send_from_spinner);
        walletsSpinner.setAdapter(arrayAdapter);
    }

    private String[] addressesFromIWalletsKeyPair(IWalletKeyPair[] wallets){
       return (String[]) Arrays.stream(wallets).map(wallet -> wallet.getHexPublicKey()).toArray();
    }


}