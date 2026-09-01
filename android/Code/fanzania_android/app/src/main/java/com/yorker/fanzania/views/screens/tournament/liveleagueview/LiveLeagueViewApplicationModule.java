package com.yorker.fanzania.views.screens.tournament.liveleagueview;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class LiveLeagueViewApplicationModule {
    private Activity activity;

    public LiveLeagueViewApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }


}
