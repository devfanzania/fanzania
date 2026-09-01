package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = MyLeagueFragmentApplicationModule.class)
public interface MyLeagueFragmentPresenterComponent {
    void inject(MyLeagueFragmentPresenter mainPresenter);
}
