package com.sdp.swiftwallet.presentation.main.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Payment fragment to send some cryptocurrency assets
 */
public class PaymentFragment extends Fragment {
    private SwiftWalletApp application;
    private Spinner walletsSpinner;
    private ArrayAdapter<String> arrayAdapter;
    private String[] walletsList;
    private TextView fromAddress;
    private TextView fromBalance;
    private TextView toAddress;

    private Web3Requests web3Requests;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (SwiftWalletApp) requireActivity().getApplication();
        walletsList = application.getWallets().getAddresses();
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
        walletsSpinner = view.findViewById(R.id.send_from_spinner);
        walletsSpinner.setAdapter(arrayAdapter);
        walletsSpinner.setOnItemSelectedListener(new OnFromAddressSelected());
    }

    private void setToSelectedAddress(String to){
        toAddress.setText(to);
    }

    private class OnFromAddressSelected implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View selectedView, int i, long l) {
            String address = ((TextView)selectedView).getText().toString();
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