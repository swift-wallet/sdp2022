package com.sdp.swiftwallet.domain.repository;

import com.sdp.swiftwallet.domain.model.User;

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
        SUCCESS, EMPTY_EMAIL, EMPTY_PASSWORD, ERROR
    }

    /**
     * Sign in to the SwiftWallet app
     *
     * @param email the email of the user
     * @param password the password of the user
     * @param success a callback to run if the user successfully logs in
     * @param failure a callback to run if the user fails to log in
     * @return the result of the authentication process
     */
    Result signIn(String email, String password, Runnable success, Runnable failure);

    /**
     * Getter for the current user
     *
     * @return None if there is no signed-in user, otherwise Some(user) if User user is signed in
     */
    Optional<User> getUser();
}
