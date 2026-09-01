package com.yorker.fanzania.views.screens.tournament.manageleague;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class,modules = ManageLeagueApplicationModule.class)
public interface ManageLeaguePresenterComponent
{
    void inject(ManageLeaguePresenter mainPresenter);
}
