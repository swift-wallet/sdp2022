package com.sdp.swiftwallet.presentation.friend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.ItemsFragment.ItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FriendItemFragment extends ItemFragment {
    private List<FriendItem> friendItems;
    private FriendItemRecyclerViewAdapter friendItemRecyclerViewAdapter;

    public FriendItemFragment() {
        super(R.layout.fragment_friend_item_list);
        friendItems = new ArrayList<>();
        friendItemRecyclerViewAdapter = new FriendItemRecyclerViewAdapter(friendItems);
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
            recyclerView.setAdapter(friendItemRecyclerViewAdapter);
        }

        return view;
    }
    //Adds a Friend Item from a keypair object
    public void addFriendItem(String friendId){
        int count = friendItemRecyclerViewAdapter.getItemCount();
        FriendItem newItem = new FriendItem(friendId);
        friendItems.add(newItem);
        friendItemRecyclerViewAdapter.notifyItemChanged(count);
    }

}