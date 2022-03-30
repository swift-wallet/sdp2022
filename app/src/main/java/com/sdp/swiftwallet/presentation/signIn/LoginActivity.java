package com.sdp.swiftwallet.presentation.signIn;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.sdp.swiftwallet.LOGIN";
    public static final String WELCOME_MESSAGE = "Welcome to SwiftWallet!";
    private static final String EMAIL_SIGNIN_TAG = "EMAIL_SIGNIN_TAG";
    private static final String GOOGLE_SIGNIN_TAG = "GOOGLE_SIGNIN_TAG";

    private TextView attemptsTextView;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> googleSignInActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attemptsTextView = (TextView) findViewById(R.id.attemptsMessage);
        attemptsTextView.setText("");

        //Init client authentication and launcher for google signIn
        mAuth = FirebaseUtil.getAuth();
        initGoogleSignInResultLauncher();

        // Set Listeners
        SignInButton googleSignInBtn = findViewById(R.id.googleSignInBtn);
        googleSignInBtn.setOnClickListener(v -> startGoogleSignIn());

        Button loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(v -> login());

      //Sets up the forgot password recover option for users
        TextView forgoPwTv = findViewById(R.id.forgotPassword);
        forgoPwTv.setOnClickListener(v ->
          startActivity(new Intent(this, ForgotPasswordActivity.class))
        );

        TextView registerTv = findViewById(R.id.register);
        registerTv.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    /**
     * Login method which is launched by the LOGIN button on the login screen
     * check email and password validity before signIn
     */
    public void login() {
        //Retrieve username and password from login screen
        EditText editText = (EditText) findViewById(R.id.loginEmail);
        String email = editText.getText().toString().trim();
        // Display error if not valid
        if (email.isEmpty()) {
            editText.setError("email required");
            editText.requestFocus();
            return;
        }
        editText = (EditText) findViewById(R.id.loginPassword);
        String password = editText.getText().toString().trim();
        if (password.isEmpty()) {
            editText.setError("password required");
            editText.requestFocus();
            return;
        }

        // Finally Launch signIn
        signInWithEmailAndPassword(email, password);
    }

    /**
     * Perform a max attempts check by updating the value and raising error if necessary
     * Also Display error if credentials were wrong
     */
    private void checkAttempts() {
        //Check if over max attempts
        if (++loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            tooManyAttemptsError(this, mainIntent).show();
            return;
        }

        //Set attempts left text
        String attemptsLeft = String.format(
                Locale.US,
                "You have %d attempt(s) remaining",
                MAX_LOGIN_ATTEMPTS - loginAttempts
        );
        attemptsTextView.setText(attemptsLeft);

        //Display error message
        incorrectCredentialsError(LoginActivity.this).show();
    }

    /**
     * Prepares the intent for the next intent to launch when the login is successful
     *
     * @param context the context of the current Activity
     * @return an Intent to launch the next activity
     */
    private Intent nextActivity(Context context) {
        Intent nextActivity = new Intent(context, MainActivity.class);
        nextActivity.putExtra(EXTRA_MESSAGE, WELCOME_MESSAGE);
        return nextActivity;
    }

    /**
     * Creates an AlertDialog to inform the user they used incorrect credentials
     *
     * @param context the context of the alert
     * @return an AlertDialog informing the user their login attempt was unsuccessful
     */
    private AlertDialog incorrectCredentialsError(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle("Unsuccessful login")
                .setMessage("Incorrect username or password")
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .create();
    }

    /**
     * Creates an AlertDialog to inform the user they have used up their login attempts
     *
     * @param context    the context of the alert
     * @param homeScreen the Intent of the Activity the user is sent to (i.e. the home screen)
     * @return an AlertDialog informing the user they used up their login attempts which also
     * starts another activity
     */
    private AlertDialog tooManyAttemptsError(Context context, Intent homeScreen) {
        return new AlertDialog.Builder(context)
                .setTitle("Too many unsuccessful attempts")
                .setMessage("You have tried to login unsuccessfully too many times")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(homeScreen);
                    }
                })
                .setCancelable(false)
                .create();
    }

    /**
     * Perform email signIn with the authentication client
     * @param email user email to connect with
     * @param password corresponding password
     */
    private void signInWithEmailAndPassword(String email, String password) {
        Log.d(EMAIL_SIGNIN_TAG, "signInWithEmailAndPassword: begin firebase auth with email account");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(EMAIL_SIGNIN_TAG, "Login successful for email: " + email);
                            Toast.makeText(LoginActivity.this, "User successfully signedIn", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else {
                            Log.w(EMAIL_SIGNIN_TAG, "Error from task", task.getException());
                            checkAttempts();
                        }
                    }
                });
    }

    /**
     * Signs-in a user using google login
     * @param account Google account
     */
    public void signInWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(GOOGLE_SIGNIN_TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        //Try to sign in the current user
        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Logged In");
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String uid = firebaseUser.getUid();
            String email = firebaseUser.getEmail();

            Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Email: "+email);
            Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: UID: "+uid);

            if (authResult.getAdditionalUserInfo().isNewUser()) {
                Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Account Created...\n"+email);
                Toast.makeText(LoginActivity.this, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Existing user...\n"+email);
                Toast.makeText(LoginActivity.this, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();
            }

            startActivity(nextActivity(this));
        }).addOnFailureListener(e -> Log.d(GOOGLE_SIGNIN_TAG, "onFailure: Loggin failed"+e.getMessage()));
    }

    /**
     * Init google SignIn launcher,
     * Perform signIn through clientAuth by giving this activity and the result activity
     */
    private void initGoogleSignInResultLauncher() {
        googleSignInActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(GOOGLE_SIGNIN_TAG, "onActivityResult: Google signIn intent result");
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                            signInWithGoogleAccount(account);
                        } catch (Exception e) {
                            Log.d(GOOGLE_SIGNIN_TAG, "onActivityResult: " + e.getMessage());
                        }
                    }
                });
    }

    /**
     * Setup GoogleSignInClient and launch google signIn with intent
     */
    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        Log.d(GOOGLE_SIGNIN_TAG, "onClick: launch Google signIn");
        Intent it = googleSignInClient.getSignInIntent();
        googleSignInActivityResultLauncher.launch(it);
    }
}