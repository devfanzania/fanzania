package com.yorker.fanzania.views.screens.auth.changepassword;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityChangePasswordBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordPresenter>
        implements ChangePasswordPresenter.IMainView {

    private ChangePasswordPresenter presenter;
    private ActivityChangePasswordBinding binding;
//    private Boolean isOldPassValidate = false;
    private Boolean isNewPassValidate = false;
    private Boolean isConfrmPassValidate = false;

    @Override
    protected ChangePasswordPresenter onCreatePresenter() {
        presenter = new ChangePasswordPresenter(this, ChangePasswordActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, ChangePasswordPresenter presenter) {
        ChangePasswordPresenterComponent component1 = DaggerChangePasswordPresenterComponent.builder()
                .presenterComponent(component)
                .changePasswordApplicationModule(new ChangePasswordApplicationModule(ChangePasswordActivity.this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);

        initViews();
        initListners();

        binding.btnSave.setOnClickListener(view -> CheckValidation());
    }

    private void initListners() {
//        binding.edtCPass.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if (s.length() > 0)
//                    presenter.CurrentPassword(binding.edtCPass.getText().toString(),
//                            binding.edtCPass, binding.txtErrorCPass);
//                else
//                    binding.txtErrorCPass.setText("");
//            }
//        });

        binding.edtNPass.addTextChangedListener(new TextWatcher() {

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
                    presenter.ValidatePassword(binding.edtNPass.getText().toString(),
                            binding.edtNPass, binding.txtErrorNPass);
                else
                    binding.txtErrorNPass.setText("");
            }
        });

        binding.edtCnfPass.addTextChangedListener(new TextWatcher() {

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
                    presenter.ConfirmPassword(binding.edtNPass.getText().toString(),
                            binding.edtCnfPass, binding.txtErrorCnfPass, binding.edtCnfPass.getText().toString());
                else
                    binding.txtErrorCnfPass.setText("");
            }
        });
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_changepassword));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnSave.setText(getString(R.string.text_save));
    }

    private void CheckValidation() {
//        if (isOldPassValidate) {
            if (isNewPassValidate) {
                if (isConfrmPassValidate) {
                    if (CheckInternetConnection()) {
                        presenter.ChangePassword(binding.edtNPass.getText().toString());
                        binding.btnSave.setEnabled(false);
                        binding.pbSave.setVisibility(View.VISIBLE);
                    } else
                        new NoNetworkDialog(this, this, Constants.APICALL_1);
                } else
                    presenter.ConfirmPassword(binding.edtNPass.getText().toString(),
                            binding.edtNPass, binding.txtErrorNPass, binding.edtCnfPass.getText().toString());
            } else
                presenter.ValidatePassword(binding.edtNPass.getText().toString(),
                        binding.edtNPass, binding.txtErrorNPass);
//        } else
//            presenter.CurrentPassword(binding.edtCPass.getText().toString(),
//                    binding.edtCPass, binding.txtErrorCPass);
    }

    @Override
    public void RetryResponse(String type) {
        presenter.ChangePassword(binding.edtNPass.getText().toString());
        binding.btnSave.setEnabled(false);
        binding.pbSave.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void CurrentPasswordvalidate(Boolean value) {
//        isOldPassValidate = value;
//    }

    @Override
    public void NewPasswordvalidate(Boolean value) {
        isNewPassValidate = value;
    }

    @Override
    public void ConfirmPasswordValidate(Boolean value) {
        isConfrmPassValidate = value;
    }

    @Override
    public void ResetEmail(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                presenter.setCustomerPassword(binding.edtNPass.getText().toString());
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_passwordsuccess));
                onBackPressed();
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
