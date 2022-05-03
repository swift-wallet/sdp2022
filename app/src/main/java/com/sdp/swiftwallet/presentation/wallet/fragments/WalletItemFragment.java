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
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A fragment representing a list of wallet items.
 */
@AndroidEntryPoint
public class WalletItemFragment extends Fragment {
    private final List<WalletItem> walletItems;
    private final WalletItemRecyclerViewAdapter walletItemRecyclerViewAdapter;

    private IWeb3Requests web3Requests;

    public WalletItemFragment() {
        walletItems = new ArrayList<>();
        walletItemRecyclerViewAdapter = new WalletItemRecyclerViewAdapter(walletItems);
    }

    public void initWeb3(IWeb3Requests web3Requests){
        this.web3Requests = web3Requests;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(walletItemRecyclerViewAdapter);
        }
        return view;
    }

    public int itemCount() {
        return walletItemRecyclerViewAdapter.getItemCount();
    }

    //Adds a Wallet Item from a keypair object
    public void addWalletItem(IWalletKeyPair keyPair){
        int count = walletItemRecyclerViewAdapter.getItemCount();
        keyPair.updateBalance(web3Requests);
        WalletItem newItem = new WalletItem(keyPair);
        walletItems.add(newItem);
        walletItemRecyclerViewAdapter.notifyItemChanged(count);
    }
}