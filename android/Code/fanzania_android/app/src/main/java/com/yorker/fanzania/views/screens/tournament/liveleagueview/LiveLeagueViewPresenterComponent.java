package com.yorker.fanzania.views.screens.tournament.liveleagueview;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = LiveLeagueViewApplicationModule.class)
public interface LiveLeagueViewPresenterComponent {
    void inject(LiveLeagueViewPresenter mainPresenter);
}
