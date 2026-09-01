package com.yorker.fanzania.views.screens.matchcontest.mcliveteamscore;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class McLiveTeamApplicationModule {
    private Activity activity;

    public McLiveTeamApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }


}
