package com.sdp.swiftwallet.presentation.friend;

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


public class FriendFragment extends Fragment{
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
            if (view.findViewById(R.id.friendCode).getVisibility() != View.VISIBLE) {
                view.findViewById(R.id.friendCode).setVisibility(View.VISIBLE);
                view.findViewById(R.id.addFriendBtn).setVisibility(View.VISIBLE);
                view.findViewById(R.id.addFriendStatus).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.friendCode).setVisibility(View.GONE);
                view.findViewById(R.id.addFriendBtn).setVisibility(View.GONE);
                view.findViewById(R.id.addFriendStatus).setVisibility(View.GONE);
            }
        });

        Button addFriendButton = view.findViewById(R.id.addFriendBtn);
        addFriendButton.setOnClickListener(v -> db.collection("friend_list")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        view.findViewById(R.id.addFriendStatus).setVisibility(View.VISIBLE);
                        String friendId = ((EditText)view.findViewById(R.id.friendCode)).getText().toString();
                        assert document != null;
                        if (friendId == mAuth.getCurrentUser().getUid()) {
                            ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Cannot Add Yourself");
                        }
                        if (!document.exists() || Objects.requireNonNull(document.getData()).get(friendId) == null) {
                            //friend request has not sent -> show friend request sent
                            db.collection("friend_list").document(mAuth.getCurrentUser().getUid())
                                    .set(Collections.singletonMap(friendId, "1"), SetOptions.merge());
                            db.collection("friend_list").document(friendId)
                                    .set(Collections.singletonMap(mAuth.getCurrentUser().getUid(), "2"), SetOptions.merge());
                            ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Friend Request Sent");
                        } else {
                            //todo: create enum for friend status
                            //1: request pending, 2: request from friend pending, 3: friends
                            switch ((String)document.getData().get(friendId)) {
                                //friend_list -> current_user_id -> friend_id : status(int)
                                //friend_list -> friend_id -> current_user_id : status(int)
                                case "1": //friend request pending -> show friend request already pending
                                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Friend Request Sent");
                                case "2": //friend request from the person pending -> show friend added
                                    db.collection("friend_list").document(mAuth.getCurrentUser().getUid())
                                            .set(Collections.singletonMap(friendId, "3"), SetOptions.merge());
                                    db.collection("friend_list").document(friendId)
                                            .set(Collections.singletonMap(mAuth.getCurrentUser().getUid(), "3"), SetOptions.merge());
                                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Friend Added");
                                case "3": //show is already friend
                                    ((TextView)view.findViewById(R.id.addFriendStatus)).setText("Is Friend");
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }));

        loadFriendItems();
        return view;
    }


    public void loadFriendItems() {
        db.collection("friend_list")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            Map <String , Object > data = document.getData();
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