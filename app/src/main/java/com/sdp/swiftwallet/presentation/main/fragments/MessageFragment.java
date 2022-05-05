package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sdp.cryptowalletapp.databinding.FragmentMessageBinding;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.Contact;
import com.sdp.swiftwallet.domain.model.ContactAdapter;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.message.AddContactActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Message fragment from main activity, display contacts
 */
@AndroidEntryPoint
public class MessageFragment extends Fragment {
    private FragmentMessageBinding binding;

    @Inject SwiftAuthenticator authenticator;
    private FirebaseFirestore db;

    // Contact list for the recyclerView
    private List<Contact> contacts;

    // Used for debugging purpose
    private CountingIdlingResource mIdlingResource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseUtil.getFirestore();

        setListeners();
        getContacts();

        return view;
    }

    /**
     * Set all listeners from MessageFragment
     */
    private void setListeners() {
        binding.addContactBtn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddContactActivity.class));
        });
    }

    /**
     * Get all contacts from user and display recyclerView
     */
    private void getContacts() {
        loading(true);
        // TODO: use cached contacts list once we have it
        // Prevent contact display offline
        User currUser = ((BaseApp) getActivity().getApplication()).getCurrUser();
        if (currUser == null) {
            loading(false);
            displayError();
            return;
        }

        String userUid = currUser.getUid();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userUid)
                .collection(Constants.KEY_COLLECTION_CONTACTS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        contacts = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                            Contact contact = new Contact();
                            contact.username = queryDocumentSnapshot.getString(Constants.KEY_USERNAME);
                            contact.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            contact.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            contacts.add(contact);
                        }
                        if (contacts.size() > 0) {
                            ContactAdapter contactAdapter = new ContactAdapter(contacts);
                            binding.contactsRecyclerView.setAdapter(contactAdapter);
                            binding.contactsRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            displayError();
                        }
                    } else {
                        displayError();
                    }
                });
    }

    /**
     * Display error message if no contact is found
     */
    private void displayError() {
        binding.textErrorMessage.setText(String.format("%s", "You have no contact"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Enable loading icon when recyclerView is building
     * @param isLoading flag to enable or disable loading
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.messageProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.messageProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}