package com.sdp.swiftwallet.presentation.main.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.presentation.friend.fragments.FriendItem;
import com.sdp.swiftwallet.presentation.friend.fragments.FriendItemFragment;
import com.sdp.swiftwallet.presentation.friend.fragments.FriendItemRecyclerViewAdapter;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MessageFragment extends Fragment{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FriendItemFragment friendItemFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseUtil.getAuth();
        db = FirebaseFirestore.getInstance();

        friendItemFragment = new FriendItemFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.friend_nested_frag_container, friendItemFragment, FriendItemFragment.class.getName())
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        Button showAddFriendButton = view.findViewById(R.id.showAddFriendBtn);
        showAddFriendButton.setOnClickListener(v -> {
            setVisible(view, view.findViewById(R.id.friendCode).getVisibility() != View.VISIBLE);
        });

        Button addFriendButton = view.findViewById(R.id.addFriendBtn);
        addFriendButton.setOnClickListener(v -> db.collection("friend_list")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        view.findViewById(R.id.addFriendStatus).setVisibility(View.VISIBLE);
                        String friendId = ((EditText)view.findViewById(R.id.friendCode)).getText().toString();
                        String status = !document.exists() ? "0" : (String)document.getData().get(friendId);
                        sendFriendRequest(view, mAuth.getCurrentUser().getUid(),
                                friendId, status);
                    }
                }));
        loadFriendItems();
        return view;
    }
    private void setVisible(View view, boolean visible) {
        view.findViewById(R.id.friendCode).setVisibility(visible ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.addFriendBtn).setVisibility(visible ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.addFriendStatus).setVisibility(View.GONE);
    }

    private void sendFriendRequest(View view, String userId, String friendId, String status) {
        if (friendId.equals(userId)) {
            ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Cannot Add Yourself");
        } else {
            //0: has not sent request, 1: request pending, 2: request from friend pending, 3: friends
            switch (status) {
                case "1": //friend request pending -> show friend request already pending
                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Friend Request Sent");
                    break;
                case "2": //friend request from the person pending -> show friend added
                    db.collection("friend_list").document(userId)
                            .set(Collections.singletonMap(friendId, "3"), SetOptions.merge());
                    db.collection("friend_list").document(friendId)
                            .set(Collections.singletonMap(userId, "3"), SetOptions.merge());
                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Friend Added");
                    break;
                case "3": //show is already friend
                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Is Friend");
                    break;
                default:
                case "0"://friend request has not sent -> show friend request sent
                    db.collection("friend_list").document(userId)
                            .set(Collections.singletonMap(friendId, "1"), SetOptions.merge());
                    db.collection("friend_list").document(friendId)
                            .set(Collections.singletonMap(userId, "2"), SetOptions.merge());
                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Friend Request Sent");
                    break;
            }
        }
    }
    public void loadFriendItems() {
        if (mAuth.getCurrentUser() != null) {
            db.collection("friend_list")
                    .document(mAuth.getCurrentUser().getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                assert data != null;
                                for (String friendId : data.keySet()) {
                                    if (data.get(friendId).toString().equals("3")) {
                                        friendItemFragment.addFriendItem(friendId);
                                    }
                                }
                            } else {
                                Log.d(TAG, "user_friend_list empty");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        }
    }
}