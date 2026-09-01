package com.yorker.fanzania.views.screens.matchcontest.scorecard;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import com.yorker.fanzania.views.screens.matchcontest.playerselection.PlayerSelectionApplicationModule;
import com.yorker.fanzania.views.screens.matchcontest.playerselection.PlayerSelectionPresenter;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = ScoreCardApplicationModule.class)
public interface ScoreCardComponent {
    void inject(ScoreCardPresenter mainPresenter);
}
