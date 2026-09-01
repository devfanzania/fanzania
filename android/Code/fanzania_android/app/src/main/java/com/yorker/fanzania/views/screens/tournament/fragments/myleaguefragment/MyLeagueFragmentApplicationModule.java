package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MyLeagueFragmentApplicationModule {

    private Activity activity;

    public MyLeagueFragmentApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }
}
