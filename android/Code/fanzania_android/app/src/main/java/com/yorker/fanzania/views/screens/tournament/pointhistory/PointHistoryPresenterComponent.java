package com.yorker.fanzania.views.screens.tournament.pointhistory;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = PointHistoryApplicationModule.class)
public interface PointHistoryPresenterComponent {
    void inject(PointHistoryPresenter mainPresenter);
}
