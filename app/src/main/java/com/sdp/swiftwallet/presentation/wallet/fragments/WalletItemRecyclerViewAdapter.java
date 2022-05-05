package com.sdp.swiftwallet.presentation.wallet.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdp.cryptowalletapp.databinding.FragmentWalletItemBinding;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.presentation.wallet.WalletSelectActivity;

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
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private Activity getActivity(View v) {
        Context context = v.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView addressView;
        public WalletItem item;

        public ViewHolder(FragmentWalletItemBinding binding) {
            super(binding.getRoot());
            addressView = binding.itemAddress;
        }

        @Override
        public void onClick(View view) {
            WalletSelectActivity walletSelectActivity = (WalletSelectActivity) getActivity(view);
            Wallets wallets = walletSelectActivity.walletProvider.getWallets();
            wallets.setCurrentKeyPair(wallets.getWalletFromAddress(item.address));
            walletSelectActivity.finish();
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