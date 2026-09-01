package com.yorker.fanzania.views.screens.splash;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.GetKeyHash;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashApplicationModule {
    private Activity activity1;

    public SplashApplicationModule(Activity activity) {
        this.activity1 = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity1);
    }

    @Provides
    GetKeyHash getKeyHash() {
        return new GetKeyHash(activity1);
    }
}
