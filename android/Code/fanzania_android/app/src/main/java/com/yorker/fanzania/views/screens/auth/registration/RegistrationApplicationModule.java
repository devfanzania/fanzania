package com.yorker.fanzania.views.screens.auth.registration;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.Validation;

import dagger.Module;
import dagger.Provides;

@Module
public class RegistrationApplicationModule {

    private Activity activity;

    public RegistrationApplicationModule(Activity activity) {
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
