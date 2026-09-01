package com.yorker.fanzania.views.screens.tournament.kyc.pan;

import android.app.Activity;

import com.yorker.fanzania.helper.GetUserData;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.Validation;

import dagger.Module;
import dagger.Provides;

@Module
public class UploadPanFragmentApplicationModule {

    private Activity activity;

    public UploadPanFragmentApplicationModule(Activity activity) {
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

    @Provides
    Validation getValidationClass() {
        return new Validation(activity);
    }
}
