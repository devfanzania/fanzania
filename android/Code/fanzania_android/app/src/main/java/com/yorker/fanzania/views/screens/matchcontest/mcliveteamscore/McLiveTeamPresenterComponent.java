package com.yorker.fanzania.views.screens.matchcontest.mcliveteamscore;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import com.yorker.fanzania.views.screens.tournament.liveteamview.LiveTeamViewApplicationModule;
import com.yorker.fanzania.views.screens.tournament.liveteamview.LiveTeamViewPresenter;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = McLiveTeamApplicationModule.class)
public interface McLiveTeamPresenterComponent {
    void inject(McLiveTeamPresenter mainPresenter);
}
