package com.yorker.fanzania.views.screens.auth.changepassword;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.Validation;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePasswordApplicationModule {
    private Activity activity;

    public ChangePasswordApplicationModule(Activity activity) {
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
