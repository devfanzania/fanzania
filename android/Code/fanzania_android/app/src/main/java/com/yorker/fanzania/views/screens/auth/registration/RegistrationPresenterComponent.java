package com.yorker.fanzania.views.screens.auth.registration;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

/**
 * Created by innofied on 26/3/18.
 */
@UserScope
@Component(dependencies = PresenterComponent.class, modules = RegistrationApplicationModule.class)
public interface RegistrationPresenterComponent {
    void inject(RegistrationPresenter mainPresenter);
}
