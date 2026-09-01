package com.yorker.fanzania.applications;

import android.app.Application;
import android.location.Location;


public class FanzaniaApplication extends Application {

    private static FanzaniaApplication mInstance;
    public static Location currentLocation = null;
    public static String address = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized FanzaniaApplication getInstance() {
        return mInstance;
    }

}
