package com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MyTeamFragmentApplicationModule {

    private Activity activity;

    public MyTeamFragmentApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }

}
