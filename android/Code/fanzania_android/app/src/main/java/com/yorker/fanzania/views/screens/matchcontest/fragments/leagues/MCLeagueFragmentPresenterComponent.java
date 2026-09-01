package com.yorker.fanzania.views.screens.matchcontest.fragments.leagues;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.MCHomeFragmentApplicationModule;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.MCHomeFragmentPresenter;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = MCLeagueFragmentApplicationModule.class)
public interface MCLeagueFragmentPresenterComponent {
    void inject(MCLeagueFragmentPresenter mainPresenter);
}
