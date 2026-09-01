package com.yorker.fanzania.views.screens.webview;

import android.app.Activity;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.GetKeyHash;

import dagger.Module;
import dagger.Provides;

@Module
public class WebviewApplicationModule {
    private Activity activity1;

    public WebviewApplicationModule(Activity activity) {
        this.activity1 = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity1);
    }
}
