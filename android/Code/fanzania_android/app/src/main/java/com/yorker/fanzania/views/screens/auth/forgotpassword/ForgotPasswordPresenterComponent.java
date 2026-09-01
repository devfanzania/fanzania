package com.yorker.fanzania.views.screens.auth.forgotpassword;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = ForgotPasswordApplicationModule.class)
public interface ForgotPasswordPresenterComponent {
    void inject(ForgotPasswordPresenter mainPresenter);
}
