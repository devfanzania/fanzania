package com.yorker.fanzania.views.screens.tournament.fragments.profilefragment;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = ProfileFragmentApplicationModule.class)
public interface ProfileFragmentPresenterComponent {
    void inject(ProfileFragmentPresenter mainPresenter);
}
