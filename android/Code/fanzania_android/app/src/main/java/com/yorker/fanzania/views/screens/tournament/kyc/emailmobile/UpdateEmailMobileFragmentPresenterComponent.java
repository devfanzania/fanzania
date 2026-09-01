package com.yorker.fanzania.views.screens.tournament.kyc.emailmobile;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.MyTeamFragmentApplicationModule;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = MyTeamFragmentApplicationModule.class)
public interface UpdateEmailMobileFragmentPresenterComponent {
    void inject(UpdateEmailMobileFragmentPresenter mainPresenter);
}
