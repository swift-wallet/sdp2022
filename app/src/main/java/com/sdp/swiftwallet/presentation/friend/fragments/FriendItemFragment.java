package com.sdp.swiftwallet.presentation.friend.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItem;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A fragment representing a list of Items.
 */
public class FriendItemFragment extends Fragment {
    private List<FriendItem> friendItems;
    private FriendItemRecyclerViewAdapter friendItemRecyclerViewAdapter;

    public FriendItemFragment() {
        friendItems = new ArrayList<>();
        friendItemRecyclerViewAdapter = new FriendItemRecyclerViewAdapter(friendItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_item_list, container, false);
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