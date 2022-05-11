package com.sdp.swiftwallet.presentation.message;

import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.cryptowalletapp.databinding.ActivityAddContactBinding;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.object.Contact;
import com.sdp.swiftwallet.domain.model.object.qrCode.QRCodeGenerator;
import com.sdp.swiftwallet.domain.model.object.qrCode.QRCodeScanner;
import com.sdp.swiftwallet.domain.model.object.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
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
    // Maps for all retrieved contacts
    private Map<String, Contact> contacts;
    // The key for the added contact
    private String contactUid;
    // The added contact
    private Contact contact;

    // Used for debugging purpose
    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseUtil.getFirestore();
        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("AddContact Calls");

        contactUid = null;
        contact = null;

        setQRCodeImage();
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

    /**
     * Callback method for the QRCode scanner
     * @param email user email
     */
    private void setEmail(String email){
        binding.addContactInputEmail.setText(email);
    }

    /**
     * Set QRCode bitmap image from user email
     */
    private void setQRCodeImage() {
        User currUser = ((BaseApp) getApplication()).getCurrUser();
        if (currUser != null) {
            try {
                String currentUserEmail = currUser.getEmail();
                binding.contactsQrView.setImageBitmap(QRCodeGenerator.encodeAsBitmap(currentUserEmail));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Search in db for user with corresponding email from input
     * Make confirm button visible if successfully found, else show error
     */
    private void searchContact() {
        previewLoading(true);
        String contactEmail = binding.addContactInputEmail.getText().toString().trim();
        Log.d(ADD_CONTACT_TAG, "searchContact: " + contactEmail);

        mIdlingResource.increment();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, contactEmail)
                .get()
                .addOnCompleteListener(task -> {
                    previewLoading(false);
                    Log.d(ADD_CONTACT_TAG, "searchContact: completed");
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(ADD_CONTACT_TAG, "searchContact: successful");

                        // search all matching contacts for this query
                        addMatchingContacts(task);
                        // pick the first matching (for now)
                        selectContact();

                        if (contactUid != null && contact != null) {
                            // update the button to add the contact once found
                            binding.previewBtnLayout.setVisibility(View.INVISIBLE);
                            binding.confirmBtnLayout.setVisibility(View.VISIBLE);
                        } else {
                            showError();
                        }
                    } else {
                        previewLoading(false);
                        showError();
                    }
                    mIdlingResource.decrement();
                });
    }

    /**
     * Add all contacts matching the query
     * @param task result from search query
     */
    private void addMatchingContacts(@NonNull Task<QuerySnapshot> task) {
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
    }

    /**
     * Take the first valid contact from the queried contacts map
     */
    private void selectContact() {
        for (Map.Entry<String, Contact> entry: contacts.entrySet()) {
            if (entry.getKey() != null) {
                contactUid = entry.getKey();
                Log.d(ADD_CONTACT_TAG, "Add contact with uid: " + contactUid);
                contact = entry.getValue();
                break;
            }
        }
    }

    /**
     * Add contact with corresponding email from input to user's contacts collection
     */
    private void addContact() {
        confirmLoading(true);

        // TODO: Change to set the cached file once User is cached
        // Prevent adding contact if offline
        User currUser = ((BaseApp) getApplication()).getCurrUser();
        if (currUser == null) {
            confirmLoading(false);
            displayToast(getApplicationContext(), "Contact registration failed");
            Log.d(ADD_CONTACT_TAG, "couldn't add contact as currUser is null");
            return;
        }

        mIdlingResource.increment();
        String userUid = currUser.getUid();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userUid)
                .collection(Constants.KEY_COLLECTION_CONTACTS)
                .document(contactUid)
                .set(contact)
                .addOnSuccessListener(unused -> {
                    confirmLoading(false);
                    displayToast(getApplicationContext(), "Contact successfully added");
                    Log.d(ADD_CONTACT_TAG, "DocumentSnapshot added with ID: " + contactUid);

                    mIdlingResource.decrement();
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
        displayToast(getApplicationContext(), "Couldn't find this user");
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