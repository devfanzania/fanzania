package com.yorker.fanzania.views.screens.matchcontest.scorecard;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ScoreCardApplicationModule {

    private Activity activity;

    public ScoreCardApplicationModule(Activity activity) {
        this.activity = activity;
    }
    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }
}
