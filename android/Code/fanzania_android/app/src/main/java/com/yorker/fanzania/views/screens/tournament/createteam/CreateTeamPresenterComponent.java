package com.yorker.fanzania.views.screens.tournament.createteam;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = CreateTeamApplicationModule.class)
public interface CreateTeamPresenterComponent {
    void inject(CreateTeamPresenter mainPresenter);
}
