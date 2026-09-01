package com.yorker.fanzania.views.screens.notification;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = NotificationApplicationModule.class)
public interface NotificationPresenterComponent {
    void inject(NotificationPresenter mainPresenter);
}
