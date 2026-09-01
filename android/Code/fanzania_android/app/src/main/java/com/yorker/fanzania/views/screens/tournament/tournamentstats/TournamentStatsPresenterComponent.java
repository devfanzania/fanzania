package com.yorker.fanzania.views.screens.tournament.tournamentstats;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = TournamentStatsApplicationModule.class)
public interface TournamentStatsPresenterComponent {
    void inject(TournamentStatsPresenter mainPresenter);
}
