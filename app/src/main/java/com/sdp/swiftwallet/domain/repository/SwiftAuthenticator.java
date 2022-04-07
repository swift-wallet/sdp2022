package com.sdp.swiftwallet.domain.repository;

import com.sdp.swiftwallet.domain.model.User;

import java.util.Optional;

public interface SwiftAuthenticator {

    enum Result {
        SUCCESS, NULL_EMAIL, NULL_PASSWORD, ERROR
    }

    Result signIn(String email, String password, Runnable success, Runnable failure);

    Optional<User> getUser();
}
