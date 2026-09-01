package com.yorker.fanzania.views.screens.matchcontest.playerselection;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayerSelectionApplicationModule {

    private Activity activity;

    public PlayerSelectionApplicationModule(Activity activity) {
        this.activity = activity;
    }
    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }
}
