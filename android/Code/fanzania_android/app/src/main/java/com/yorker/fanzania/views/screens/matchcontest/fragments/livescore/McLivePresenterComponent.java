package com.yorker.fanzania.views.screens.matchcontest.fragments.livescore;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = McLiveApplicationModule.class)
public interface McLivePresenterComponent {
    void inject(McLivePresenter mainPresenter);
}
