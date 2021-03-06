package com.sdp.swiftwallet;

import android.app.Application;
import android.content.Context;

import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;

/**
 * For hilt testing, it is required to use an "intermediate" class that first extends Application
 * but wihtout the @HiltAndroidApp tag.
 * Hence this app should be used when doing casting to get application context;
 * See LoginActivity for instances.
 */
public class BaseApp extends Application {
    private final TransactionHistoryProducer transactionHistoryProducer = null;
    private User currUser = null;

    private static Context context;

    public BaseApp(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * Getter for the ApplicationContext
     *
     * @return the Context of this Application
     */
    public static Context getAppContext() {
        return BaseApp.context;
    }

    /**
     * Getter for the currently signed in user
     * @return
     */
    public User getCurrUser() {
        return currUser;
    }

    /**
     * Setter for the current user
     * @param user the user which is signed in
     */
    public void setCurrUser(User user) {
        this.currUser = user;
    }
}
