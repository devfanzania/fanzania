package com.yorker.fanzania.views.screens.tournament.manageleague;

import android.content.Context;

import com.yorker.fanzania.presenter.PresenterStub;

public class ManageLeaguePresenter extends PresenterStub
{
    private IMainView activitycallback;
    private Context context;


    public ManageLeaguePresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }



    public interface IMainView
    {
    }

}
