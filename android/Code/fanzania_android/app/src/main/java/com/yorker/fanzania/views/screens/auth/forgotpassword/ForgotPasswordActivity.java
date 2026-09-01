package com.yorker.fanzania.views.screens.auth.forgotpassword;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityForgotPasswordBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

public class ForgotPasswordActivity extends BaseActivity<ForgotPasswordPresenter> implements ForgotPasswordPresenter.IMainView {

    private ForgotPasswordPresenter presenter;
    private ActivityForgotPasswordBinding binding;

    @Override
    protected ForgotPasswordPresenter onCreatePresenter() {
        presenter = new ForgotPasswordPresenter(this, ForgotPasswordActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, ForgotPasswordPresenter presenter) {
        ForgotPasswordPresenterComponent forgotPasswordPresenterComponent = DaggerForgotPasswordPresenterComponent.builder()
                .presenterComponent(component)
                .forgotPasswordApplicationModule(new ForgotPasswordApplicationModule(ForgotPasswordActivity.this))
                .build();
        forgotPasswordPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        initView();
        initListner();
    }

    private void initListner() {
        binding.edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                presenter.validateEmail(binding.edtEmail.getText().toString(), binding.edtEmail, binding.txtErrorEmail);
                binding.txtErrorEmail.setText("");
            }
        });

        binding.btnSubmit.setOnClickListener(view -> {
            if (CheckInternetConnection())
                presenter.ForgotPassword(binding.edtEmail.getText().toString());
            else
                new NoNetworkDialog(this, this, Constants.APICALL_2);
        });
    }

    private void initView() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_retrivepassword));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnSubmit.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.EmailChecking(binding.edtEmail.getText().toString());
                break;

            case Constants.APICALL_2:
                presenter.ForgotPassword(binding.edtEmail.getText().toString());
                break;
        }
    }

    @Override
    public void Emailvalidate(boolean b) {
        if (b) {

            if (CheckInternetConnection())
                presenter.EmailChecking(binding.edtEmail.getText().toString());
            else
                new NoNetworkDialog(this, this, Constants.APICALL_1);
        }
    }

    @Override
    public void EmailExistsChecking(boolean b) {
        if (b) {
            binding.pbSubmit.setEnabled(false);
            binding.btnSubmit.setEnabled(false);
            binding.txtErrorEmail.setText(getString(R.string.text_emailnotexists));
            binding.txtErrorEmail.setVisibility(View.VISIBLE);
        } else {
            binding.pbSubmit.setEnabled(true);
            binding.btnSubmit.setEnabled(true);
            binding.txtErrorEmail.setText("");
            binding.txtErrorEmail.setVisibility(View.GONE);
        }
    }

    @Override
    public void ForgotPasswordResponse(boolean b, String txt) {
        CustomToast.getInstance(this).showLongCustomToast(txt);

        if (b)
            onBackPressed();
    }
}
