package com.yorker.fanzania.views.screens.tournament.kyc;

import android.content.Context;

import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;

import javax.inject.Inject;

public class KYCPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    SharedPrefManager sharedPrefManager;


    public KYCPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {

    }
}
