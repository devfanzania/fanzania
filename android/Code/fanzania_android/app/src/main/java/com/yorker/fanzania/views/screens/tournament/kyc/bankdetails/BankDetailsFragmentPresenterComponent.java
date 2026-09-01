package com.yorker.fanzania.views.screens.tournament.kyc.bankdetails;

import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.qualifier.UserScope;
import dagger.Component;

@UserScope
@Component(dependencies = PresenterComponent.class, modules = BankDetailsFragmentApplicationModule.class)
public interface BankDetailsFragmentPresenterComponent {
    void inject(BankDetailsFragmentPresenter mainPresenter);
}
