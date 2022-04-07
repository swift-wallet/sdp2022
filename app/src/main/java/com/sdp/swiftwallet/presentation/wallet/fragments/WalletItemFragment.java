package com.sdp.swiftwallet.presentation.wallet.fragments;

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
import com.sdp.swiftwallet.presentation.ItemsFragment.ItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of wallet items.
 */
public class WalletItemFragment extends ItemFragment {
    private final List<WalletItem> walletItems;
    private final WalletItemRecyclerViewAdapter walletItemRecyclerViewAdapter;

    public WalletItemFragment() {
        super(R.layout.fragment_wallet_item_list);
        walletItems = new ArrayList<>();
        walletItemRecyclerViewAdapter = new WalletItemRecyclerViewAdapter(walletItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(list, container, false);
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
        WalletItem newItem = new WalletItem(keyPair);
        walletItems.add(newItem);
        walletItemRecyclerViewAdapter.notifyItemChanged(count);
    }
}