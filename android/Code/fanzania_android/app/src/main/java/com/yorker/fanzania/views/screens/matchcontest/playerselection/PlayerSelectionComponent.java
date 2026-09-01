package com.yorker.fanzania.views.screens.matchcontest.playerselection;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import com.yorker.fanzania.views.screens.matchcontest.myallmatches.MyAllMatchApplicationModule;
import com.yorker.fanzania.views.screens.matchcontest.myallmatches.MyAllMatchPresenter;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = PlayerSelectionApplicationModule.class)
public interface PlayerSelectionComponent {
    void inject(PlayerSelectionPresenter mainPresenter);
}
