package com.yorker.fanzania.dependencyinjection;

import android.app.Activity;

import dagger.Module;

@Module
public class ApplicationModule {

    Activity activity;

    public ApplicationModule(Activity activity) {
        this.activity = activity;
    }


}
