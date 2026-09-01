package com.yorker.fanzania.views.screens.notification;

import android.app.Activity;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class NotificationApplicationModule {
    private Activity activity1;

    public NotificationApplicationModule(Activity activity) {
        this.activity1 = activity;
    }

    @Provides
    SharedPrefManager getSharedPrefManager() {
        return new SharedPrefManager(activity1);
    }
}
