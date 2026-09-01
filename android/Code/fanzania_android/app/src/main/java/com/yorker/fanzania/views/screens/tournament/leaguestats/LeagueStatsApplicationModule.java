package com.yorker.fanzania.views.screens.tournament.leaguestats;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

import dagger.Module;
import dagger.Provides;

@Module
public class LeagueStatsApplicationModule {
    private Activity activity;

    public LeagueStatsApplicationModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity);
    }


}
