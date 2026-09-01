package com.yorker.fanzania.views.screens.auth.landingpage;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = LandingApplicationModule.class)
public interface LandingPresenterComponent {
    void inject(LandingPresenter mainPresenter);
}
