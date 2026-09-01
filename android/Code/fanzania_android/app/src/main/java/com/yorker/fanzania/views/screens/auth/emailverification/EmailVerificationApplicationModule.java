package com.yorker.fanzania.views.screens.auth.emailverification;

import android.app.Activity;

import com.yorker.fanzania.helper.GetUserData;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class EmailVerificationApplicationModule {
    private Activity activity;

    public EmailVerificationApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }

    @Provides
    GetUserData getGetUserData() {
        return new GetUserData();
    }

}
