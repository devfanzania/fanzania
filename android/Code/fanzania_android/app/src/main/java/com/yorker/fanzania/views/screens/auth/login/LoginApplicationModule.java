package com.yorker.fanzania.views.screens.auth.login;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.Validation;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginApplicationModule {
    private Activity activity;

    public LoginApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Validation getValidationClass() {
        return new Validation(activity);
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }
}
