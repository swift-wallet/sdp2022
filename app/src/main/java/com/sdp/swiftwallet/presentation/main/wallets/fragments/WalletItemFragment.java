package com.sdp.swiftwallet.presentation.wallets;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A fragment representing a list of Items.
 */
public class WalletItemFragment extends Fragment {
    private List<WalletItem> walletItems;
    private WalletItemRecyclerViewAdapter walletItemRecyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletItemFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletItems = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_item_list, container, false);
        walletItemRecyclerViewAdapter = new WalletItemRecyclerViewAdapter(walletItems);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(walletItemRecyclerViewAdapter);
        }
        return view;
    }
    //Adds a Wallet Item from a keypair object
    public void addWalletItem(WalletKeyPair keyPair){
        int count = walletItemRecyclerViewAdapter.getItemCount();
        WalletItem newItem = new WalletItem(keyPair.getHexPublicKey());
        //This is only for testing purposes
        newItem.updateBalance(Math.abs(new Random().nextDouble()));
        walletItems.add(newItem);
        walletItemRecyclerViewAdapter.notifyItemChanged(count);
    }
}