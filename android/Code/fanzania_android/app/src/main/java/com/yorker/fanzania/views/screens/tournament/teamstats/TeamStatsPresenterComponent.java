package com.yorker.fanzania.views.screens.tournament.teamstats;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = TeamStatsApplicationModule.class)
public interface TeamStatsPresenterComponent {
    void inject(TeamStatsPresenter mainPresenter);
}
