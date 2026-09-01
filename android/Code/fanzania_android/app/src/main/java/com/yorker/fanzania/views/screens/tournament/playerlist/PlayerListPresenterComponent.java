package com.yorker.fanzania.views.screens.tournament.playerlist;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = PlayerListApplicationModule.class)
public interface PlayerListPresenterComponent {
    void inject(PlayerListPresenter mainPresenter);
}
