package com.yorker.fanzania.views.screens.auth.emailverification;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityEmailVerificationBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.views.shared.model.UserDetailsModel;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailVerificationActivity extends BaseActivity<EmailVerificationPresenter> implements EmailVerificationPresenter.IMainView {

    private EmailVerificationPresenter presenter;
    private ActivityEmailVerificationBinding binding;
    private String verificationCode = "";
    private UserDetailsModel userData;

    @Override
    protected EmailVerificationPresenter onCreatePresenter() {
        presenter = new EmailVerificationPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, EmailVerificationPresenter presenter) {
        EmailVerificationPresenterComponent component1 = DaggerEmailVerificationPresenterComponent.builder()
                .presenterComponent(component)
                .emailVerificationApplicationModule(new EmailVerificationApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_verification);

        getData();

        setView();

        setListners();
    }

    private void getData() {
        if (getIntent().getStringExtra(Constants.TAG_USERDETAILS) != null) {
            userData = presenter.getUserData(getIntent().getStringExtra(Constants.TAG_USERDETAILS));

            if (getIntent().getIntExtra(Constants.TAG_CLASS, 0) == 1)
                getEmailVerificationCode();
            else{
                verificationCode=userData.getActivationToken();
                binding.llMain.setVisibility(View.VISIBLE);
                binding.pbVerification.setVisibility(View.GONE);

                binding.btnResend.setEnabled(true);
                binding.pbResend.setVisibility(View.GONE);
            }
        }
    }

    private void setListners() {
        binding.btnResend.setOnClickListener(view -> {
            if (CheckInternetConnection()) {
                presenter.getVerificationCode(userData.getUserId(), userData.getEmail());
                binding.btnResend.setEnabled(false);
                binding.pbResend.setVisibility(View.VISIBLE);
            } else
                new NoNetworkDialog(this, this, Constants.APICALL_3);
        });
    }

    private void getEmailVerificationCode() {
        if (CheckInternetConnection())
            presenter.getVerificationCode(userData.getUserId(), userData.getEmail());
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void setView() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_verification));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnResend.setText(getString(R.string.text_resend));

        if (binding.txtPinEntry2 != null) {
            binding.txtPinEntry2.setOnPinEnteredListener(str -> {
                System.out.println("verification code " + verificationCode);
                if (str.toString().equals(verificationCode)) {
                    if (CheckInternetConnection()) {
                        presenter.setEmailVerified(userData.getUserId());
                        binding.llMain.setVisibility(View.GONE);
                        binding.pbVerification.setVisibility(View.VISIBLE);
                    } else
                        new NoNetworkDialog(this, this, Constants.APICALL_2);

                } else {
                    binding.txtPinEntry2.setError(true);
                    CustomToast.getInstance(this).showLongCustomToast(getString(R.string.text_verificationcodenotvalid));
                    binding.txtPinEntry2.postDelayed(() -> binding.txtPinEntry2.setText(null), 700);
                }
            });
        }
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.getVerificationCode(userData.getUserId(), userData.getEmail());
                break;

            case Constants.APICALL_2:
                presenter.setEmailVerified(userData.getUserId());
                binding.llMain.setVisibility(View.GONE);
                binding.pbVerification.setVisibility(View.VISIBLE);
                break;

            case Constants.APICALL_3:
                presenter.getVerificationCode(userData.getUserId(), userData.getEmail());
                binding.btnResend.setEnabled(false);
                binding.pbResend.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void VerificationCode(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                verificationCode = jsonObject1.getString("ActivationToken");
                CustomToast.getInstance(this).showLongCustomToast(getString(R.string.text_codetxt));
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.llMain.setVisibility(View.VISIBLE);
        binding.pbVerification.setVisibility(View.GONE);

        binding.btnResend.setEnabled(true);
        binding.pbResend.setVisibility(View.GONE);
    }

    @Override
    public void setEmailVerified(JSONObject jsonObject) {

        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                if (jsonObject1.getBoolean("Active")) {

                    presenter.SaveCustomer_id(userData.getUserId());

                    presenter.SaveCustomerName(userData.getName());

                    presenter.SaveCustomer_Email(userData.getEmail());

                    presenter.SaveAuthToken(userData.getSessionId());

                    startActivity(new Intent(this, HomeActivity.class));
                    finishAffinity();
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.llMain.setVisibility(View.VISIBLE);
        binding.pbVerification.setVisibility(View.GONE);
    }
}
