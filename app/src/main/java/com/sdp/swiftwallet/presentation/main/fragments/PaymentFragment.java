package com.sdp.swiftwallet.presentation.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;

/**
 * Payment fragment to send some cryptocurrency assets
 */
public class PaymentFragment extends Fragment {
    private ArrayAdapter<String> arrayAdapter;
    private TextView fromAddress;
    private TextView fromBalance;
    private TextView toAddress;

    private final OnFromAddressSelected onFromAddressSelected = new OnFromAddressSelected();
    QRCodeScanner qrCodeScanner = new QRCodeScanner(this::setToSelectedAddress, this);
    private Web3Requests web3Requests;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwiftWalletApp application = (SwiftWalletApp) requireActivity().getApplication();
        String[] walletsList = application.getWallets().getAddresses();
        arrayAdapter = new ArrayAdapter<String>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, walletsList);
        web3Requests = new Web3Requests();
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
        fromAddress = view.findViewById(R.id.send_from_address);
        fromBalance = view.findViewById(R.id.send_from_balance);
        toAddress = view.findViewById(R.id.send_to_address);

        // Initialize the address spinner
        Spinner walletsSpinner = view.findViewById(R.id.send_from_spinner);
        walletsSpinner.setAdapter(arrayAdapter);
        walletsSpinner.setOnItemSelectedListener(onFromAddressSelected);

        view.findViewById(R.id.send_qr_scan).setOnClickListener(v -> qrCodeScanner.launch());
    }

    private void setToSelectedAddress(String to){
        toAddress.setText(to);
    }

    private class OnFromAddressSelected implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View selectedView, int i, long l) {
            String address = arrayAdapter.getItem(i);
            fromAddress.setText(address);
            web3Requests.getBalanceOf(address).thenAccept(balance->{
                fromBalance.setText(balance.toString(10));
            });
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }
}