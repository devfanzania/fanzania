package com.yorker.fanzania.views.screens.auth.registration;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityRegistrationNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.auth.emailverification.EmailVerificationActivity;
import com.yorker.fanzania.views.screens.webview.WebviewActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends BaseActivity<RegistrationPresenter> implements RegistrationPresenter.IMainView {

    private RegistrationPresenter presenter;
    private ActivityRegistrationNewBinding binding;
    private Boolean isPasswordValid = false;
    private String referralCode = "";

    @Override
    protected RegistrationPresenter onCreatePresenter() {
        presenter = new RegistrationPresenter(this, RegistrationActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, RegistrationPresenter presenter) {
        RegistrationPresenterComponent registrationPresenterComponent = DaggerRegistrationPresenterComponent.builder()
                .presenterComponent(component)
                .registrationApplicationModule(new RegistrationApplicationModule(this))
                .build();
        registrationPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_new);

        setView();

        setFieldsBackground();

        setListners();
    }

    private void setFieldsBackground() {

    }

    private void setListners() {
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

                presenter.CheckEmail(binding.edtEmail.getText().toString());

                binding.txtErrorEmail.setText("");

            }
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {

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

                if (s.length() > 0)
                    presenter.ValidatePassword(binding.edtPassword.getText().toString(), binding.edtPassword, binding.txtErrorPassword);
                else {
                    isPasswordValid=false;
                    binding.txtErrorPassword.setText("");
                }
            }
        });

        binding.edtName.addTextChangedListener(new TextWatcher() {

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

                binding.txtErrorName.setText("");
                binding.txtErrorName.setVisibility(View.GONE);
            }
        });

        binding.btnSignUp.setOnClickListener(view -> Checkvalidation());

        binding.txtTermsCondition.setOnClickListener(view -> startActivity(new Intent(this, WebviewActivity.class)
                .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_TNC)));

        binding.btnSupport.setOnClickListener(view->startActivity(new Intent(this, WebviewActivity.class)
                .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_CONTACTUS)));
    }

    private void setView() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.toolbar.setTitle("");

        setSupportActionBar(binding.toolbar);

        if (binding.toolbar != null)
            binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnSignUp.setText(getString(R.string.text_signup));
    }

    private void Checkvalidation() {
        presenter.NameChecking(binding.edtName.getText().toString(), binding.edtName, binding.txtErrorName);
    }

    @Override
    public void Namevalidate(boolean b) {
        if (b)
            presenter.validateEmail(binding.edtEmail.getText().toString(), binding.edtEmail, binding.txtErrorEmail);
    }

    @Override
    public void EmailChecking(boolean b) {
        if (b) {
            binding.btnSignUp.setEnabled(true);
            binding.txtErrorEmail.setText("");
            binding.txtErrorEmail.setVisibility(View.GONE);
        } else {
            binding.btnSignUp.setEnabled(false);
            binding.txtErrorEmail.setText(getString(R.string.text_emailexists));
            binding.txtErrorEmail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void CheckEmail(boolean b) {
        if (CheckInternetConnection())
            presenter.EmailChecking(binding.edtEmail.getText().toString());
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    @Override
    public void Emailvalidate(boolean b) {
        if (b)
            if (isPasswordValid) {
                if (CheckInternetConnection()) {

                    if (binding.edtReferral.getText().toString().length()>0)
                        referralCode=binding.edtReferral.getText().toString();

                    presenter.Registration(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString(),
                            binding.edtName.getText().toString(),
                            referralCode
                    );
                    binding.btnSignUp.setEnabled(false);
                    binding.pbSignUp.setVisibility(View.VISIBLE);
                } else
                    new NoNetworkDialog(this, this, Constants.APICALL_1);
            } else
                presenter.ValidatePassword(binding.edtPassword.getText().toString(), binding.edtPassword, binding.txtErrorPassword);
    }

    @Override
    public void Passwordvalidate(boolean b) {
        isPasswordValid = b;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void RegistrationResponse(JSONObject jsonObject) {

        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                presenter.SaveCustomer_Password(binding.edtPassword.getText().toString());

                CustomToast.getInstance(this).showLongCustomToast(getString(R.string.text_registrationsuccess));

                startActivity(new Intent(RegistrationActivity.this, EmailVerificationActivity.class)
                        .putExtra(Constants.TAG_USERDETAILS, jsonObject1.toString())
                        .putExtra(Constants.TAG_CLASS, 0)
                );
                finish();

            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.btnSignUp.setEnabled(true);
        binding.pbSignUp.setVisibility(View.GONE);
    }

    @Override
    public void RetryResponse(String type) {

        switch (type) {
            case Constants.APICALL_1:
                presenter.Registration(binding.edtEmail.getText().toString(),
                        binding.edtPassword.getText().toString(),
                        binding.edtName.getText().toString(),
                        referralCode
                );
                binding.btnSignUp.setEnabled(false);
                binding.pbSignUp.setVisibility(View.VISIBLE);
                break;

            case Constants.APICALL_2:
                presenter.EmailChecking(binding.edtEmail.getText().toString());
                break;
        }

    }
}
