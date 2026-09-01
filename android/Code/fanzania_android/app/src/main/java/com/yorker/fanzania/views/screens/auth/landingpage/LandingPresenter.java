package com.yorker.fanzania.views.screens.auth.landingpage;

import android.content.Context;
import com.yorker.fanzania.presenter.PresenterStub;

public class LandingPresenter extends PresenterStub {

    private IMainView activitycallback;

    public LandingPresenter(IMainView activitycallback1, Context context) {
        activitycallback = activitycallback1;
        Context mContext = context;
    }

    interface IMainView {

    }
}
