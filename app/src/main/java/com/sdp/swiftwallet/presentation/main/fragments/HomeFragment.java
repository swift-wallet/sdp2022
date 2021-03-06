package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.idling.CountingIdlingResource;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.di.wallet.WalletProvider;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.google.zxing.WriterException;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.di.wallet.WalletProvider;
import com.sdp.swiftwallet.domain.model.qrCode.QRCodeGenerator;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.WalletSelectActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Home screen
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private View fragmentView;
    private ImageView qrView;

    private BigInteger etherBalance;

    private double etherValue;

    @Inject
    public WalletProvider walletProvider;

    @Inject
    public IWeb3Requests web3Requests;

    private boolean hasSeed;

    // For testing purposes
    private CountingIdlingResource mIdlingResource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdlingResource = new CountingIdlingResource("CryptoValue Calls");
        hasSeed = walletProvider.hasWallets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentView.findViewById(R.id.seed_setup).setOnClickListener(this::launchSeedSetup);
        fragmentView.findViewById(R.id.first_wallet_setup_button).setOnClickListener(this::launchWalletSelector);
        fragmentView.findViewById(R.id.home_to_select).setOnClickListener(this::launchWalletSelector);
        fragmentView.findViewById(R.id.home_copy_address).setOnClickListener(this::copyAddressToClipBoard);
        qrView = fragmentView.findViewById(R.id.home_qr);
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
        etherBalance = walletKeyPair.getNativeBalance();
        ((TextView)fragmentView.findViewById(R.id.item_address))
                .setText(HelperFunctions.toShortenedFormatAddress(walletKeyPair.getHexPublicKey()));
        ((TextView)fragmentView.findViewById(R.id.ether_balance))
                .setText(etherBalance.toString());
        mIdlingResource.increment();
        getCurrentValue("ETH");

        walletProvider.getWallets().setCurrentKeyPair(walletKeyPair);
    }

    //Request the current value of the crypto
    private void getCurrentValue(String name){
        String url = "https://api.binance.com/api/v3/ticker/24hr?symbol="+name+"USDT";
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                String value = response.getString("lastPrice");
                etherValue = Double.parseDouble(value);
                ((TextView)fragmentView.findViewById(R.id.worth)).setText(Double.toString(etherBalance.doubleValue()*etherValue)+"$");
                mIdlingResource.decrement();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this.getActivity(), "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            etherValue = 0.0;
            Toast.makeText(this.getActivity(), "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(jsonObjectRequest);

    }

    // Resetting the view to handle wallets list
    private void recoverWalletView() {
        IWalletKeyPair currentKeyPair = walletProvider.getWallets().getCurrentKeyPair();
        if(currentKeyPair != null){
            setSelectedWallet(currentKeyPair);
            fragmentView.findViewById(R.id.wallet_container).setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.first_wallet_setup_layout).setVisibility(View.GONE);
            generateQR();
        } else {
            fragmentView.findViewById(R.id.first_wallet_setup_layout).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Generates the QR code
     */
    private void generateQR() {
        Bitmap bitmap = null;
        try {
            bitmap = QRCodeGenerator.encodeAsBitmap(walletProvider.getWallets().getCurrentKeyPair().getHexPublicKey());
        } catch (WriterException exception){
            exception.printStackTrace();
        }
        if (bitmap != null){
            qrView.setImageBitmap(bitmap);
        }
    }

    private void copyAddressToClipBoard(View v){
        ClipData clip = ClipData.newPlainText(getString(R.string.account_label), walletProvider.getWallets().getCurrentKeyPair().getHexPublicKey());
        ((ClipboardManager)requireContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(clip);
        Toast.makeText(requireContext(), "Copied address", Toast.LENGTH_SHORT).show();
    }

    private void launchSeedSetup(View v) {
        Intent intent = new Intent(requireActivity(), CreateSeedActivity.class);
        startActivity(intent);
    }

    private void launchWalletSelector(View v) {
        Intent selectorIntent = new Intent(requireContext(), WalletSelectActivity.class);
        // Maybe add extra of already selected wallet later ?
        startActivity(selectorIntent);
    }
}
