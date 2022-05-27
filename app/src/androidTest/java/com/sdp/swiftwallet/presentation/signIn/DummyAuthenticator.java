package com.sdp.swiftwallet.presentation.signIn;

import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;

import java.util.Optional;

public class DummyAuthenticator implements SwiftAuthenticator {

    SwiftAuthenticator.Result result;

    User currUser;

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
    public void signOut() {
        currUser = null;
    }

    @Override
    public Result sendPasswordResetEmail(String email, Runnable success, Runnable failure) {
        if (execSuccess) {
            success.run();
        }

        if (execFailure) {
            failure.run();
        }

        return result;
    }

    @Override
    public Result updateUserEmail(String email, Runnable success, Runnable failure) {
        if (execSuccess) {
            success.run();
        }

        if (execFailure) {
            failure.run();
        }

        return result;
    }

    @Override
    public User getUser() {
        return currUser;
    }

    @Override
    public String getUid() {
        if (currUser != null) {
            return currUser.getUid();
        }
        else {
            return null;
        }
    }

    @Override
    public void setLanguageCode(String code) {
    }

    public void setResult(SwiftAuthenticator.Result result) {
        this.result = result;
    }

    public void setCurrUser(User user) {
        this.currUser = user;
    }

    public void setExecSuccess(boolean execSuccess) {
        this.execSuccess = execSuccess;
    }

    public void setExecFailure(boolean execFailure) {
        this.execFailure = execFailure;
    }

}
