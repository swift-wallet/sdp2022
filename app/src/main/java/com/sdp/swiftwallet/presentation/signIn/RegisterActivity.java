package com.sdp.swiftwallet.presentation.signIn;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.checkPassword;
import static com.sdp.swiftwallet.common.HelperFunctions.checkUsername;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.R;
import com.sdp.cryptowalletapp.databinding.ActivityRegisterBinding;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.User;

import javax.inject.Inject;

public class RegisterActivity extends AppCompatActivity {
    // Register TAG for logs
    private static final String EMAIL_REGISTER_TAG = "EMAIL_REGISTER_TAG";
    private static final int IMAGE_PREVIEW_WIDTH = 150;
    private static final int IMAGE_PREVIEW_QUALITY = 50;

    private ActivityRegisterBinding binding;
    private String encodedImage;

    // Authentication and database
    @Inject
    FirebaseAuth mAuth;
    @Inject
    FirebaseFirestore db;

    // Used for debugging purpose
    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Image is null if no image is selected
        encodedImage = null;

        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("Register Calls");

        setListeners();
    }

    /**
     * Set all listeners from registerActivity
     */
    private void setListeners() {
        binding.goBackRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
        binding.registerBtn.setOnClickListener(v -> registerUser());
        binding.registerImageProfileLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            selectImage.launch(intent);
        });
    }

    /**
     * Enable loading icon for register button
     * @param isLoading flag to enable or disable loading
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.registerBtn.setVisibility(View.INVISIBLE);
            binding.registerBtnProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.registerBtnProgressBar.setVisibility(View.INVISIBLE);
            binding.registerBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Check for registration infos validity and create user on client auth
     */
    private void registerUser() {
        String username = binding.registerInputUsername.getText().toString().trim();
        String email = binding.registerInputEmail.getText().toString().trim();
        String password = binding.registerInputPassword.getText().toString().trim();
        String confirmPassword = binding.registerInputConfirmPassword.getText().toString().trim();

        // Check validity of inputs
        if (encodedImage == null) encodedImage = Constants.DEFAULT_USER_IMAGE;
        if (!checkUsername(username, binding.registerInputUsername)) return;
        if (!checkEmail(email, binding.registerInputEmail)) return;
        if (!checkPassword(password, confirmPassword, binding.registerInputPassword)) return;

        // Increment counter before creating user
        mIdlingResource.increment();
        loading(true);
        createUserWithEmailAndPassword(username, email, password);
    }

    /**
     * Base64 image encoder from Bitmap to String
     * @param bitmap the image bitmap
     * @return String of encoded image in Base64
     */
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = IMAGE_PREVIEW_WIDTH;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_PREVIEW_QUALITY, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Launcher to get encoded image from returned image
     */
    private final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.registerImageProfileBackground.setImageBitmap(bitmap);
                            binding.registerImageProfileText.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Map<String, Object> setUser(String uid, String username, String email, String image) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(Constants.KEY_UID, uid);
        userMap.put(Constants.KEY_USERNAME, username);
        userMap.put(Constants.KEY_EMAIL, email);
        userMap.put(Constants.KEY_IMAGE, image);

        return userMap;
    }

    /**
     * create user with username, email and password with client auth
     * @param username user username
     * @param email user email
     * @param password user password
     */
    private void createUserWithEmailAndPassword(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(EMAIL_REGISTER_TAG, "User created for Auth");
                        Map<String, Object> userMap = setUser(mAuth.getUid(), username, email, encodedImage);
                        // Increment before adding to db and decrement because user is created
                        mIdlingResource.increment();
                        registerUserToDatabase(userMap);
                        mIdlingResource.decrement();
                    }
                    else {
                        loading(false);
                        // If user failed to create an account, decrement counter
                        mIdlingResource.decrement();
                        displayToast(getApplicationContext(), "User failed to register");
                        Log.w(EMAIL_REGISTER_TAG, "Error from task", task.getException());
                    }
                });
    }

    /**
     * Add a user to the database and go back to login activity
     * @param userMap the user HashMap to add to the database
     */
    private void registerUserToDatabase(Map<String, Object> userMap) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(mAuth.getUid())
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    loading(false);
                    displayToast(getApplicationContext(), "User successfully registered");
                    Log.d(EMAIL_REGISTER_TAG, "DocumentSnapshot added with ID: " + mAuth.getUid());

                    // Decrement counter if user successfully added to db
                    mIdlingResource.decrement();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                })
                .addOnFailureListener(e -> {
                    loading(false);
                    // Decrement counter if user failed to add to db
                    mIdlingResource.decrement();
                    displayToast(getApplicationContext(), "User failed to register");
                    Log.w(EMAIL_REGISTER_TAG, "Error adding document", e);
                });
    }

    /**
     * Getter for the idling resource (used only in testCase normally)
     * @return the idling resource used by RegisterActivity
     */
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }
}