package com.sdp.swiftwallet.presentation.friend.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;
import com.sdp.cryptowalletapp.databinding.FragmentFriendItemBinding;

import java.util.List;


public class FriendItemRecyclerViewAdapter extends RecyclerView.Adapter<FriendItemRecyclerViewAdapter.ViewHolder> {

    private final List<FriendItem> mValues;
    public FriendItemRecyclerViewAdapter(List<FriendItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_friend_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = mValues.get(position);
        holder.addressView.setText(mValues.get(position).address);
//        holder.balanceView.setText(String.valueOf(mValues.get(position).balance));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView addressView;
        public FriendItem item;

        public ViewHolder(View view) {
            super(view);
            addressView = (TextView) view.findViewById(R.id.item_address);
//            balanceView = binding.itemBalance;
        }
    }
}