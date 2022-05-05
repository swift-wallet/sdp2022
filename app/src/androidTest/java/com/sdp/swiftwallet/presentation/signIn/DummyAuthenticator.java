package com.sdp.swiftwallet.presentation.signIn;

import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;

import java.util.Optional;

public class DummyAuthenticator implements SwiftAuthenticator {

    SwiftAuthenticator.Result result;

    boolean execSuccess;
    boolean execFailure;

    public static DummyAuthenticator INSTANCE = new DummyAuthenticator();

    @Override
    public Result signIn(String email, String password, Runnable success, Runnable failure) {
        if (execSuccess) {
            success.run();
        }

        if (execFailure) {
            failure.run();
        }

        return result;
    }

    @Override
    public Result signUp(String username, String email, String password, Runnable success, Runnable failure) {
        if (execSuccess) {
            success.run();
        }

        if (execFailure) {
            failure.run();
        }

        return result;
    }

    @Override
    public Optional<User> getUser() {
        return Optional.empty();
    }

    public void setResult(SwiftAuthenticator.Result result) {
        this.result = result;
    }

    public void setExecSuccess(boolean execSuccess) {
        this.execSuccess = execSuccess;
    }

    public void setExecFailure(boolean execFailure) {
        this.execFailure = execFailure;
    }

}
