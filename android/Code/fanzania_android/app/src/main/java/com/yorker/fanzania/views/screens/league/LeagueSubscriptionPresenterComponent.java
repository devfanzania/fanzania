package com.yorker.fanzania.views.screens.league;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = LeagueSubscriptionApplicationModule.class)
public interface LeagueSubscriptionPresenterComponent {
    void inject(LeagueSubscriptionPresenter mainPresenter);
}
