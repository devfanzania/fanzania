package com.yorker.fanzania.views.screens.tournament.kyc.emailmobile;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratLight;
import com.yorker.fanzania.databinding.FragmentUpdateEmailMobileBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateEmailMobileFragment extends BaseFragment<UpdateEmailMobileFragmentPresenter> implements UpdateEmailMobileFragmentPresenter.IMainView {

    private UpdateEmailMobileFragmentPresenter presenter;
    private FragmentUpdateEmailMobileBinding binding;
    String activationToken = null;

    @Override
    protected UpdateEmailMobileFragmentPresenter onCreatePresenter() {
        presenter = new UpdateEmailMobileFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, UpdateEmailMobileFragmentPresenter presenter) {
//        UploadPanFragmentPresenterComponent uploadPanFragmentPresenterComponent = DaggerUploadPanFragmentPresenterComponent.builder()
//                .presenterComponent(component)
//                .myTeamFragmentApplicationModule(new UploadPanFragmentApplicationModule(getActivity()))
//                .build();
//        uploadPanFragmentPresenterComponent.inject(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_update_email_mobile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListners();
//        getData();

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
        binding.edtName.setText(sharedPrefManager.getCustomer_Email());
        String mobile = sharedPrefManager.getCustomer_Phone() == null || sharedPrefManager.getCustomer_Phone().equals("null") ? "" : sharedPrefManager.getCustomer_Phone();
        binding.edtNumber.setText(mobile);

        if (mobile.length() <= 1){
            binding.sendOTP.setVisibility(View.GONE);
            binding.sendOTPNote.setVisibility(View.VISIBLE);
        }

        getData();

    }

    private void getData() {
        if (CheckInternetConnection())
            presenter.fetchDetails();
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_1);
    }

    private void initListners() {

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
//                    binding.btnSave.setEnabled(false);
//                    binding.btnSave.setClickable(false);
//                    updateData();
                }
            }
        });

        binding.sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        binding.txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        binding.verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyVerificationCode();
            }
        });
    }

    private void sendVerificationCode() {
        if (CheckInternetConnection()){
            presenter.sendVerificationCode();
            binding.pBar.setVisibility(View.VISIBLE);
        }
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_2);

    }

    private void verifyVerificationCode() {
        if (CheckInternetConnection()){
            presenter.verifyVerificationCode(binding.edtOTP.getText().toString());
            binding.pBar.setVisibility(View.VISIBLE);
        }
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_2);

    }

    private boolean validate() {

        boolean result = false;
        if(binding.edtName.getText().toString().length() == 0){
            binding.txtErrorName.setText(getString(R.string.required));
            binding.txtErrorName.setVisibility(View.VISIBLE);
            return result = false;
        }else if(!isValidEmail(binding.edtName.getText())){
            binding.txtErrorName.setText(getString(R.string.text_email_not_valid));
            binding.txtErrorName.setVisibility(View.VISIBLE);
            return result = false;
        }else {
            binding.txtErrorName.setVisibility(View.GONE);
            result = true;
        }
        if(binding.edtNumber.getText().toString().length() == 0){
            binding.txtErrorNumber.setText(getString(R.string.required));
            binding.txtErrorNumber.setVisibility(View.VISIBLE);
            return result = false;
        }else if(!PhoneChecking(binding.edtNumber.getText().toString(),binding.edtNumber, binding.txtErrorNumber)){
            return result = false;
        }else{
            binding.txtErrorNumber.setVisibility(View.GONE);
            result = true;
        }

        return result;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public Boolean PhoneChecking(String str_phone, EditText field, MontserratLight txt){
        if (TextUtils.isDigitsOnly(str_phone) && str_phone.length()>9){
            txt.setText(null);
            txt.setVisibility(View.GONE);
            return true;
        } else {
            field.requestFocus();
            txt.setText(getString(R.string.text_phonenumberisnotvalid));
            txt.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void initView() {

    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.fetchDetails();
                break;
            case Constants.APICALL_2:
                sendVerificationCode();
                break;

            case Constants.APICALL_3:
                verifyVerificationCode();
                break;

        }
    }

    @Override
    public void KYCDetails(JSONObject jsonObject) {
        Log.e("details",""+jsonObject);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                String status = jsonObject1.getString("KYCStatus");
                String MobileVerified = jsonObject1.getString("MobileVerified");
                if (MobileVerified.equalsIgnoreCase("yes")){
                    binding.phoneVerifyImg.setVisibility(View.VISIBLE);
                    binding.sendOTP.setVisibility(View.GONE);
//                    binding.sendOTPNote.setVisibility(View.VISIBLE);
                }else
                if(status.equalsIgnoreCase("approved")){
                    binding.phoneVerifyImg.setVisibility(View.INVISIBLE);
                    binding.sendOTP.setVisibility(View.VISIBLE);
                    binding.sendOTPNote.setVisibility(View.GONE);
                }
                else{
                    binding.phoneVerifyImg.setVisibility(View.INVISIBLE);
                    binding.sendOTP.setVisibility(View.GONE);
                    binding.sendOTPNote.setVisibility(View.VISIBLE);
                }

            } else
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnVerificationCodeSent(JSONObject jsonObject) {
        try {
            binding.pBar.setVisibility(View.GONE);
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                JSONObject jsonObject1 = jsonObject.getJSONObject(Constants.STR_DATA);
                activationToken = jsonObject1.getString("ActivationToken");
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));
                verificationCodeSent();
            } else{
                activationToken = null;
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            activationToken = null;
        }
    }

    @Override
    public void OnVerificationCodeVerified(JSONObject jsonObject) {
        try {
            binding.pBar.setVisibility(View.GONE);
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));
                verificationCodeVerified();
                presenter.fetchDetails();
            } else{
                activationToken = null;
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            activationToken = null;
        }
    }

    private void verificationCodeSent() {
        binding.enterOTPRL.setVisibility(View.VISIBLE);
        binding.sendOTP.setVisibility(View.GONE);
        binding.edtOTP.setText("");
    }

    private void verificationCodeVerified() {
        binding.enterOTPRL.setVisibility(View.GONE);
        binding.sendOTP.setVisibility(View.GONE);
    }

    @Override
    public void KYCUploadStatus(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                presenter.fetchDetails();
            } else
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnFailed(Boolean b, String msg) {
        binding.pBar.setVisibility(View.GONE);
        CustomToast.getInstance(getContext()).showSmallCustomToast(msg);
    }
}

