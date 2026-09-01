package com.yorker.fanzania.views.screens.tournament.fragments.homefragment;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = HomeFragmentApplicationModule.class)
public interface HomeFragmentPresenterComponent {
    void inject(HomeFragmentPresenter mainPresenter);
}
