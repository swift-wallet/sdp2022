package com.sdp.swiftwallet.domain.repository;

import com.sdp.swiftwallet.domain.model.User;

import java.util.Optional;

public interface SwiftAuthenticator {

    enum Result {
        SUCCESS, NULL_EMAIL, NULL_PASSWORD
    }

    class Credentials {
        private final String email;
        private final String password;

        public Credentials(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    Result signIn(Optional<Credentials> credentials, Runnable success, Runnable failure);

    Optional<User> getUser();
}
