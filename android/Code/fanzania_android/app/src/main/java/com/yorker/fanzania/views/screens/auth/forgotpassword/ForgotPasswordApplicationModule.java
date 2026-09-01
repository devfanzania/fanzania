package com.yorker.fanzania.views.screens.auth.forgotpassword;

import android.app.Activity;

import com.yorker.fanzania.views.shared.Validation;

import dagger.Module;
import dagger.Provides;

@Module
public class ForgotPasswordApplicationModule {
    private Activity activity;

    public ForgotPasswordApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Validation getValidationClass() {
        return new Validation(activity);
    }
}
