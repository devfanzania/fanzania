package com.yorker.fanzania.views.screens.Home;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = HomeApplicationModule.class)
public interface HomePresenterComponent {
    void inject(HomePresenter mainPresenter);
}
