package com.yorker.fanzania.views.screens.tournament.wallet;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = WalletApplicationModule.class)
public interface WalletPresenterComponent {
    void inject(WalletPresenter mainPresenter);
}
