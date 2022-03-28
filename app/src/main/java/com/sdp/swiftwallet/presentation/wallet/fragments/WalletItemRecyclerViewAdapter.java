package com.sdp.swiftwallet.presentation.wallet.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdp.cryptowalletapp.databinding.FragmentWalletItemBinding;
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
        holder.addressView.setText(item.getAddress());
        holder.balanceView.setText(item.getBalance());
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView addressView;
        public final TextView balanceView;
        public WalletItem item;

        public ViewHolder(FragmentWalletItemBinding binding) {
            super(binding.getRoot());
            addressView = binding.itemAddress;
            balanceView = binding.itemBalance;
        }

        @Override
        public void onClick(View view) {
            Intent walletInfoIntent = new Intent(view.getContext(), WalletInfoActivity.class);
            walletInfoIntent.putExtra(WalletInfoActivity.ADDRESS_EXTRA, item.getAddress());
            walletInfoIntent.putExtra(WalletInfoActivity.BALANCE_EXTRA, item.getBalance());
            view.getContext().startActivity(walletInfoIntent);
        }
    }
}