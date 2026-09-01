package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = LiveScoreFragmentApplicationModule.class)
public interface LiveScoreFragmentPresenterComponent {
    void inject(LiveScoreFragmentPresenter mainPresenter);
}
