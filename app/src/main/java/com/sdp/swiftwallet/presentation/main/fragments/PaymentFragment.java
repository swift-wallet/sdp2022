package com.sdp.swiftwallet.presentation.main.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.di.WalletProvider;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Payment fragment to send some cryptocurrency assets
 */
@AndroidEntryPoint
public class PaymentFragment extends Fragment {
    private ArrayAdapter<String> arrayAdapter;
    private TextView fromAddress;
    private TextView fromBalance;
    private TextView toAddress;
    private EditText sendAmount;
    private SeekBar seekBar;

    private boolean isSeeking = false;

    private final OnFromAddressSelected onFromAddressSelected = new OnFromAddressSelected();
    QRCodeScanner qrCodeScanner = new QRCodeScanner(this::setToSelectedAddress, this);
    private Web3Requests web3Requests;

    @Inject
    public WalletProvider walletProvider;

    private String[] addresses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking if a wallets object exist, if yes import the addresses
        if(walletProvider.hasWallets()){
            addresses = walletProvider.getWallets().getAddresses();
        }
        arrayAdapter = new ArrayAdapter<String>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, addresses);
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

        // Seek bar and send amount
        sendAmount = view.findViewById(R.id.send_amount);
        seekBar = view.findViewById(R.id.send_amount_seekBar);
        // When amount changes, adapt the seek bar's progress
        sendAmount.addTextChangedListener(new AmountWatcher());
        // When seekbar is touched by user, adapt the amount
        seekBar.setOnSeekBarChangeListener(new SeekBarWatcher());

        // Initializes the address spinner
        Spinner walletsSpinner = view.findViewById(R.id.send_from_spinner);
        walletsSpinner.setAdapter(arrayAdapter);
        walletsSpinner.setOnItemSelectedListener(onFromAddressSelected);

        view.findViewById(R.id.send_qr_scan).setOnClickListener(v -> qrCodeScanner.launch());
    }

    private void setToSelectedAddress(String to){
        toAddress.setText(to);
    }

    /**
     * Those private classes are implementation needed for some view listeners of this fragment
     */

    private class OnFromAddressSelected implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View selectedView, int i, long l) {
            // On item selected we want the from field view to adapt
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

    private class SeekBarWatcher implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(isSeeking){
                float amount = seekBar.getProgress() * Float.parseFloat(fromBalance.getText().toString()) / 100;
                sendAmount.setText(Float.toString(amount));
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeeking = true;
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeeking = false;
        }
    }

    private class AmountWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if(sendAmount.getText().toString().equals("")) return;
            float sendValue = Float.parseFloat(sendAmount.getText().toString());
            float maxValue = Float.parseFloat(fromBalance.getText().toString());
            float progressPercentage;
            if(sendValue > maxValue){
                progressPercentage = 100;
                sendAmount.setText(Float.toString(maxValue));
            }else if(sendValue < 0){
                progressPercentage = 0;
                sendAmount.setText(0);
            }else{
                progressPercentage = (sendValue / maxValue)*100;
            }
            seekBar.setProgress((int)progressPercentage);
        }
    }
}