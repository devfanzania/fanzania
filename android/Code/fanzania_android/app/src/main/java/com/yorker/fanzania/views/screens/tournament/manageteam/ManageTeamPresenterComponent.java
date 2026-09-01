package com.yorker.fanzania.views.screens.tournament.manageteam;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = ManageTeamApplicationModule.class)
public interface ManageTeamPresenterComponent {
    void inject(ManageTeamPresenter mainPresenter);
}
