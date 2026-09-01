package com.yorker.fanzania.views.screens.webview;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import com.yorker.fanzania.views.screens.splash.SplashApplicationModule;
import com.yorker.fanzania.views.screens.splash.SplashPresenter;

import dagger.Component;

/**
 * Created by innofied on 26/3/18.
 */
@UserScope
@Component(dependencies = PresenterComponent.class, modules = WebviewApplicationModule.class)
public interface WebviewComponent {
    void inject(WebviewPresenter mainPresenter);
}
