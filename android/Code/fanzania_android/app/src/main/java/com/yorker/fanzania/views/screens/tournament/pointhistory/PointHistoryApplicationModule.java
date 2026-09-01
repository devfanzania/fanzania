package com.yorker.fanzania.views.screens.tournament.pointhistory;

import android.app.Activity;

import dagger.Module;

@Module
public class PointHistoryApplicationModule {
    private Activity activity;

    public PointHistoryApplicationModule(Activity activity) {
        this.activity = activity;
    }

}
