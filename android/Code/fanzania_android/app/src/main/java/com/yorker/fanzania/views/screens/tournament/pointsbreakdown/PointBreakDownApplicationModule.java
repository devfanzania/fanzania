package com.yorker.fanzania.views.screens.tournament.pointsbreakdown;

import android.app.Activity;

import dagger.Module;

@Module
public class PointBreakDownApplicationModule {
    private Activity activity;

    public PointBreakDownApplicationModule(Activity activity) {
        this.activity = activity;
    }

}
