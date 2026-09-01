package com.yorker.fanzania.views.screens.tournament.manageleague;

import android.app.Activity;

import dagger.Module;

@Module
public class ManageLeagueApplicationModule
{
    private Activity activity;

    public ManageLeagueApplicationModule(Activity activity) {
        this.activity = activity;
    }

}
