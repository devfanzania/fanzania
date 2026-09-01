package com.yorker.fanzania.views.screens.matchcontest.fragments.home;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MCHomeFragmentApplicationModule {

    private Activity activity;

    public MCHomeFragmentApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }
}
