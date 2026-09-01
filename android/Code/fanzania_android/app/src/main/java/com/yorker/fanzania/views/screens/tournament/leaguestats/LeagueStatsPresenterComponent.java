package com.yorker.fanzania.views.screens.tournament.leaguestats;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = LeagueStatsApplicationModule.class)
public interface LeagueStatsPresenterComponent {
    void inject(LeagueStatsPresenter mainPresenter);
}
