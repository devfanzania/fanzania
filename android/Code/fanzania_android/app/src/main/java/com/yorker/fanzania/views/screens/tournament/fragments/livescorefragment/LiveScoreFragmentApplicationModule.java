package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class LiveScoreFragmentApplicationModule {

    private Activity activity;

    public LiveScoreFragmentApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }
}
