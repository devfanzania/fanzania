package com.yorker.fanzania.views.screens.auth.landingpage;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ActivityLandingBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.views.screens.auth.login.LoginActivity;
import com.yorker.fanzania.views.screens.auth.registration.RegistrationActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

public class LandingActivity extends BaseActivity<LandingPresenter> implements LandingPresenter.IMainView {

    @Override
    protected LandingPresenter onCreatePresenter() {
        return new LandingPresenter(this, this);
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LandingPresenter presenter) {
        LandingPresenterComponent component1 = DaggerLandingPresenterComponent.builder()
                .presenterComponent(component)
                .landingApplicationModule(new LandingApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLandingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);

        binding.btnSignIn.setOnClickListener(view->startActivity(new Intent(this, LoginActivity.class)));

        binding.btnSignUp.setOnClickListener(view->startActivity(new Intent(this, RegistrationActivity.class)));
    }

    @Override
    public void RetryResponse(String type) {

    }
}
