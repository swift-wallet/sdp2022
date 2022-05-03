package com.sdp.swiftwallet.presentation.wallet.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdp.cryptowalletapp.databinding.FragmentWalletItemBinding;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.presentation.wallet.WalletInfoActivity;

import java.util.List;


public class WalletItemRecyclerViewAdapter extends RecyclerView.Adapter<WalletItemRecyclerViewAdapter.ViewHolder> {

    private final List<WalletItem> mValues;
    public WalletItemRecyclerViewAdapter(List<WalletItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentWalletItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        WalletItem item = mValues.get(position);
        holder.item = item;
        holder.addressView.setText(HelperFunctions.toShortenedFormatAddress(item.getAddress()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView addressView;
        public WalletItem item;

        public ViewHolder(FragmentWalletItemBinding binding) {
            super(binding.getRoot());
            addressView = binding.itemAddress;
        }
    }

    protected static class WalletItem {
        private final String address;

        public WalletItem(IWalletKeyPair walletKeyPair) {
            this.address = walletKeyPair.getHexPublicKey();
        }
        public String getAddress(){
            return address;
        }
    }
}