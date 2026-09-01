package com.yorker.fanzania.views.screens.tournament.pointsbreakdown;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = PointBreakDownApplicationModule.class)
public interface PointBreakDownPresenterComponent {
    void inject(PointBreakDownPresenter mainPresenter);
}
