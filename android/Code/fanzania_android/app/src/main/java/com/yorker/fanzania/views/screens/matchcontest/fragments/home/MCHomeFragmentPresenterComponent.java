package com.yorker.fanzania.views.screens.matchcontest.fragments.home;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = MCHomeFragmentApplicationModule.class)
public interface MCHomeFragmentPresenterComponent {
    void inject(MCHomeFragmentPresenter mainPresenter);
}
