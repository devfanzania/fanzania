package com.yorker.fanzania.views.screens.auth.landingpage;

import android.app.Activity;

import dagger.Module;

@Module
public class LandingApplicationModule {
    private Activity activity1;

    public LandingApplicationModule(Activity activity) {
        this.activity1 = activity;
    }

}
