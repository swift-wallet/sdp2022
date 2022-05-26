package com.sdp.swiftwallet.domain.repository.firebase;

import android.widget.EditText;
import com.sdp.swiftwallet.domain.model.User;
import java.util.Optional;

/**
 * SwiftAuthenticator interfaces
 */
public interface SwiftAuthenticator {

    /**
     * The result of the authentication process SUCCESS: there were no errors EMPTY_EMAIL: an empty
     * email was provided EMPTY_PASSWORD: an empty password was provided ERROR: an unexpected error
     * occurred
     */
    enum Result {
        SUCCESS, EMPTY_USERNAME, EMPTY_EMAIL, EMPTY_PASSWORD, ERROR, ERROR_NOT_ONLINE, ERROR_EMAIL_FORMAT
    }

    /**
     * BASIC: basic (BASIC email + pw login) GOOGLE: google login
     */
    enum LoginMethod {
        BASIC, GOOGLE
    }

    /**
     * Sign in to the SwiftWallet app
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @param success  a callback to run if the user successfully logs in
     * @param failure  a callback to run if the user fails to log in
     * @return the result of the authentication process
     */
    Result signIn(String email, String password, Runnable success, Runnable failure);

    /**
     * Sign up to the SwiftWallet app
     * @param username the username of the user
     * @param email the email of the user
     * @param password the password of the user
     * @param success a callback to run if the user successfully register
     * @param failure a callback to run if the user fails to register
     * @return the result of the registration process
     */
    Result signUp(String username, String email, String password, Runnable success,
        Runnable failure);

    /**
     * Signs out the current user (never fails)
     * @param  success action to take upon sign out
     * @param  failure to take upon sign out
     * @return specific ERROR or SUCCESS
     */
    Result signOut(Runnable success, Runnable failure);

    /**
     * Updates the email of the suer
     *
     * @param email      email of the user
     * @param emailField emailField where he types his email
     * @param success    success behavior
     * @param failure    error behavior
     * @return specific ERROR or SUCCESS
     */
    Result updateEmail(String email, EditText emailField, Runnable success, Runnable failure);

    /**
     * Send a reset password email to change user password
     * @param email the user email
     * @param success a callback to run if the email is successfully sent
     * @param failure a callback to run if the sending failed
     * @return the result of the sending process
     */
    Result sendPasswordResetEmail(String email, Runnable success, Runnable failure);

    /**
     * Getter for the current user
     *
     * @return None if there is no signed-in user, otherwise Some(user) if User user is signed in
     */
    Optional<User> getUser();

    /**
     * Getter for the current user uid
     * @return None if there is no signed-in user, otherwise Some(uid) if User user is signed in
     */
    Optional<String> getUid();

    /**
     * Setter for the user-facing language code
     */
    void setLanguageCode(String code);
}
