package com.yorker.fanzania.views.screens.auth.login;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

/**
 * Created by innofied on 26/3/18.
 */
@UserScope
@Component(dependencies = PresenterComponent.class, modules = LoginApplicationModule.class)
public interface LoginPresenterComponent {
    void inject(LoginPresenter mainPresenter);
}
