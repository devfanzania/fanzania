package com.yorker.fanzania.views.screens.matchcontest.fragments.teams;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = McTeamFragmentApplicationModule.class)
public interface McTeamFragmentPresenterComponent {
    void inject(McTeamFragmentPresenter mainPresenter);
}
