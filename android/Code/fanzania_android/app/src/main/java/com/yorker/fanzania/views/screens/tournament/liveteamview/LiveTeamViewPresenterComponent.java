package com.yorker.fanzania.views.screens.tournament.liveteamview;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = LiveTeamViewApplicationModule.class)
public interface LiveTeamViewPresenterComponent {
    void inject(LiveTeamViewPresenter mainPresenter);
}
