package com.yorker.fanzania.views.screens.splash;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

/**
 * Created by innofied on 26/3/18.
 */
@UserScope
@Component(dependencies = PresenterComponent.class, modules = SplashApplicationModule.class)
public interface SplashPresenterComponent {
    void inject(SplashPresenter mainPresenter);
}
