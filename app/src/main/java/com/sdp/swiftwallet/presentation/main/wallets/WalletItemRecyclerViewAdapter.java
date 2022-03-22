package com.sdp.swiftwallet.presentation.main.wallets;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdp.cryptowalletapp.databinding.FragmentWalletItemBinding;

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
        holder.item = mValues.get(position);
        holder.addressView.setText(mValues.get(position).address);
        holder.balanceView.setText(String.valueOf(mValues.get(position).balance));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView addressView;
        public final TextView balanceView;
        public WalletItem item;

        public ViewHolder(FragmentWalletItemBinding binding) {
            super(binding.getRoot());
            addressView = binding.itemAddress;
            balanceView = binding.itemBalance;
        }
    }
}