package com.sdp.swiftwallet.presentation.message;

import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.WriterException;
import com.sdp.cryptowalletapp.databinding.ActivityAddContactBinding;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.Contact;
import com.sdp.swiftwallet.domain.model.QRCodeGenerator;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddContactActivity extends AppCompatActivity {

    private static final String ADD_CONTACT_TAG = "ADD_CONTACT_TAG";
    private ActivityAddContactBinding binding;

    @Inject
    SwiftAuthenticator authenticator;
    private FirebaseFirestore db;

    // For now just log the scanned email
    QRCodeScanner qrCodeScanner = new QRCodeScanner(this::setEmail, this);

    // Used for debugging purpose
    private CountingIdlingResource mIdlingResource;

    private Map<String, Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseUtil.getFirestore();
        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("AddContact Calls");

        try {
            String currentUserEmail = ((SwiftWalletApp)getApplication()).getCurrUser().getEmail();
            binding.contactsQrView.setImageBitmap(QRCodeGenerator.encodeAsBitmap(currentUserEmail));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        setListeners();
    }

    /**
     * Set all listeners from AddContactActivity
     */
    private void setListeners() {
        binding.previewBtn.setOnClickListener(v -> searchContact());
        binding.confirmBtn.setOnClickListener(v -> addContact());
        binding.contactsViaQrButton.setOnClickListener(v -> qrCodeScanner.launch());
    }

    private void setEmail(String email){
        binding.addContactInputEmail.setText(email);
    }

    /**
     * Search in db for user with corresponding email from input
     * Make confirm addition visible if successfully found, else show error
     */
    private void searchContact() {
        mIdlingResource.increment();
        previewLoading(true);
        String contactEmail = binding.addContactInputEmail.getText().toString().trim();
        Log.d(ADD_CONTACT_TAG, "searchContact: " + contactEmail);

        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, contactEmail)
                .get()
                .addOnCompleteListener(task -> {
                    mIdlingResource.decrement();
                    previewLoading(false);
                    Log.d(ADD_CONTACT_TAG, "searchContact: completed");
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(ADD_CONTACT_TAG, "searchContact: successful");
                        contacts = new HashMap<>();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            String contactUID = documentSnapshot.getString(Constants.KEY_UID);
                            Contact contact = new Contact();
                            contact.username = documentSnapshot.getString(Constants.KEY_USERNAME);
                            contact.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            contact.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            Log.d(ADD_CONTACT_TAG, "put contact with uid: " + contactUID);
                            contacts.put(contactUID, contact);
                        }
                        // TODO: showPreview()
                        binding.previewBtnLayout.setVisibility(View.INVISIBLE);
                        binding.confirmBtnLayout.setVisibility(View.VISIBLE);
                    } else {
                        mIdlingResource.decrement();
                        previewLoading(false);
                        showError();
                    }
                });
    }

    /**
     * Add contact with corresponding email from input to user's contacts collection
     */
    private void addContact() {
        mIdlingResource.increment();
        confirmLoading(true);

        // TODO: change this behaviour later
        // select the first found
        String contactUID = null;
        Contact contact = null;
        for (Map.Entry<String, Contact> entry: contacts.entrySet()) {
            if (entry.getKey() != null) {
                contactUID = entry.getKey();
                Log.d(ADD_CONTACT_TAG, "Add contact with uid: " + contactUID);
                contact = entry.getValue();
                break;
            }
        }
        String userKey = ((SwiftWalletApp) getApplication()).getCurrUser().getUid();
        String finalContactUID = contactUID;
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userKey)
                .collection(Constants.KEY_COLLECTION_CONTACTS)
                .document(contactUID)
                .set(contact)
                .addOnSuccessListener(unused -> {
                    mIdlingResource.decrement();
                    confirmLoading(false);
                    displayToast(getApplicationContext(), "Contact successfully added");
                    Log.d(ADD_CONTACT_TAG, "DocumentSnapshot added with ID: " + finalContactUID);

                    // TODO: make it come back to message fragment
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                })
                .addOnFailureListener(e -> {
                    mIdlingResource.decrement();
                    confirmLoading(false);
                    displayToast(getApplicationContext(), "Contact registration failed");
                    Log.w(ADD_CONTACT_TAG, "Error adding document", e);
                });
    }

    /**
     * Display error message to user
     */
    private void showError() {
        // TODO: Add an error message like in MessageFragment or make a Toast
        Log.d(ADD_CONTACT_TAG, "Error searching for contacts");
    }

    /**
     * Enable loading icon for preview button
     * @param isLoading flag to enable or disable loading
     */
    private void previewLoading(Boolean isLoading) {
        if (isLoading) {
            binding.previewBtn.setVisibility(View.INVISIBLE);
            binding.previewBtnProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.previewBtnProgressBar.setVisibility(View.INVISIBLE);
            binding.previewBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Enable loading icon for confirm button
     * @param isLoading flag to enable or disable loading
     */
    private void confirmLoading(Boolean isLoading) {
        if (isLoading) {
            binding.confirmBtn.setVisibility(View.INVISIBLE);
            binding.confirmBtnProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.confirmBtnProgressBar.setVisibility(View.INVISIBLE);
            binding.confirmBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Getter for the idling resource (used only in testCase normally)
     * @return the idling resource used by AddContactActivity
     */
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }
}