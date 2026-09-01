package com.yorker.fanzania.views.screens.auth.changepassword;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = ChangePasswordApplicationModule.class)
public interface ChangePasswordPresenterComponent {
    void inject(ChangePasswordPresenter mainPresenter);
}
