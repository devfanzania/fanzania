package com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = MyTeamFragmentApplicationModule.class)
public interface MyTeamFragmentPresenterComponent {
    void inject(MyTeamFragmentPresenter mainPresenter);
}
