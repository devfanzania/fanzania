package com.yorker.fanzania.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface PresenterComponent {
}
