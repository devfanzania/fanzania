package com.yorker.fanzania.views.screens.tournament.pointhistory;

import android.content.Context;

import com.yorker.fanzania.presenter.PresenterStub;

public class PointHistoryPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    public PointHistoryPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {

    }
}
