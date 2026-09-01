package com.yorker.fanzania.views.screens.tournament.pointsbreakdown;

import android.content.Context;

import com.yorker.fanzania.presenter.PresenterStub;

public class PointBreakDownPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    public PointBreakDownPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {

    }
}
