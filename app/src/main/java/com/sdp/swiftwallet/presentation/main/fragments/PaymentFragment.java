package com.sdp.swiftwallet.presentation.main.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.di.WalletProvider;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.TransactionHelper;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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

    private Button sendButton;

    private boolean isSeeking = false;

    private final OnFromAddressSelected onFromAddressSelected = new OnFromAddressSelected();
    QRCodeScanner qrCodeScanner = new QRCodeScanner(this::setToSelectedAddress, this);

    @Inject
    public IWeb3Requests web3Requests;

    @Inject
    public WalletProvider walletProvider;

    private String[] addresses;
    private HashMap<String, IWalletKeyPair> addressToKeyPair;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressToKeyPair = new HashMap<>();
        // Checking if a wallets object exist, if yes import the addresses
        if(walletProvider.hasWallets()){
            recoverAddresses();
        }else{
            addresses = new String[]{};
        }
        arrayAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, addresses);
    }

    /**
     * Initializing the string array for the UI and a mapping between each addresses
     * and their WalletKeyPair object
     */
    private void recoverAddresses(){
        Wallets wallets = walletProvider.getWallets();
        addresses = wallets.getAddresses();
        int len = addresses.length;
        for (int i=0; i<len ; i++) {
            addressToKeyPair.put(addresses[i], wallets.getWalletFromId(i));
        }
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
        // Send button listener
        sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(this::send);

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

    @Override
    public void onResume() {
        super.onResume();
        if(walletProvider.hasWallets()){
            recoverAddresses();
        }
    }

    private void setToSelectedAddress(String to){
        toAddress.setText(to);
    }

    /**
     * Initiates the transaction
     * @param v the button view
     */
    private void send(View v) {
        IWalletKeyPair from = addressToKeyPair.get(fromAddress.getText().toString());
        String to = toAddress.getText().toString();
        double amount = Double.parseDouble(sendAmount.getText().toString()) * 1000;
        BigInteger bigAmount = new BigInteger(String.valueOf(Math.round(amount)));
        // Ethereum values are factored by 1e18
        bigAmount = bigAmount.multiply(new BigInteger("1000000000000000"));
        // We create a raw transaction
        CompletableFuture<RawTransaction> rawTransaction = TransactionHelper
                .createTransaction(web3Requests, from.getHexPublicKey(), to, bigAmount);
        // We send the raw transaction to the blockchain
        rawTransaction.thenAccept(rT -> {
            String hexTransaction = from.signTransaction(rT);
            web3Requests.sendTransaction(hexTransaction);
            Toast.makeText(requireContext(), "Transaction: " + hexTransaction, Toast.LENGTH_SHORT).show();
        });
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

    /**
     * Watcher
     */
    private class AmountWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if (sendAmount.getText().toString().equals("")) return;
            float sendValue = Float.parseFloat(sendAmount.getText().toString());
            float maxValue = Float.parseFloat(fromBalance.getText().toString());
            float progressPercentage;
            if (sendValue > maxValue){
                progressPercentage = 100;
                sendAmount.setText(Float.toString(maxValue));
            } else if(sendValue < 0){
                progressPercentage = 0;
                sendAmount.setText(0);
            } else{
                progressPercentage = (sendValue / maxValue)*100;
            }
            seekBar.setProgress((int)progressPercentage);
        }
    }
}