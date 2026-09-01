package com.yorker.fanzania.views.screens.matchcontest.myallmatches;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = MyAllMatchApplicationModule.class)
public interface MyAllMatchComponent {
    void inject(MyAllMatchPresenter mainPresenter);
}
