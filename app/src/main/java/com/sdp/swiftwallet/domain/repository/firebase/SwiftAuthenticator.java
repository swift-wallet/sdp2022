package com.sdp.swiftwallet.domain.repository.firebase;

import com.sdp.swiftwallet.domain.model.object.User;
import java.util.Optional;

/**
 * SwiftAuthenticator interfaces
 */
public interface SwiftAuthenticator {

    /**
     * The result of the authentication process
     * SUCCESS: there were no errors
     * EMPTY_EMAIL: an empty email was provided
     * EMPTY_PASSWORD: an empty password was provided
     * ERROR: an unexpected error occurred
     */
    enum Result {
        SUCCESS, EMPTY_USERNAME, EMPTY_EMAIL, EMPTY_PASSWORD, ERROR
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
     * @return the result of the registration process
     */
    Result signUp(String username, String email, String password, Runnable success, Runnable failure);

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
}
