package com.yorker.fanzania.views.screens.tournament.kyc.bankdetails;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
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
import com.yorker.fanzania.databinding.FragmentUpdateBankDetailsBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.screens.tournament.kyc.emailmobile.UpdateEmailMobileFragmentPresenter;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class BankDetailsFragment extends BaseFragment<BankDetailsFragmentPresenter> implements BankDetailsFragmentPresenter.IMainView, UpdateEmailMobileFragmentPresenter.IMainView {

    private BankDetailsFragmentPresenter presenter;
    private UpdateEmailMobileFragmentPresenter presenterMobile;
    private FragmentUpdateBankDetailsBinding binding;
    private String bankName, accountNumber,ifsc;

    @Override
    protected BankDetailsFragmentPresenter onCreatePresenter() {
        presenter = new BankDetailsFragmentPresenter(this, getActivity());
        presenterMobile = new UpdateEmailMobileFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, BankDetailsFragmentPresenter presenter) {
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
                inflater, R.layout.fragment_update_bank_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListners();
        getData();
        getKYCData();

    }

    private void getData() {
        if (CheckInternetConnection())
            presenter.fetchDetails();
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_1);
    }

    private void getKYCData() {
        if (CheckInternetConnection())
            presenterMobile.fetchDetails();
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_2);
    }

    private void updateData() {
        if (CheckInternetConnection()){
            binding.pBar.setVisibility(View.VISIBLE);
            presenter.updateDetails(bankName, ifsc,accountNumber);
        }
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_1);
    }

    private void initListners() {

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    binding.btnSave.setEnabled(false);
                    binding.btnSave.setClickable(false);
                    updateData();
                }
            }
        });

    }

    private boolean validate() {

        boolean result = false;

        bankName = binding.edtName.getText().toString();
        ifsc = binding.edtIfsc.getText().toString();
        accountNumber = binding.edtNumber.getText().toString();

        if(binding.edtName.getText().toString().length() == 0){
            binding.txtErrorName.setText(getString(R.string.required));
            binding.txtErrorName.setVisibility(View.VISIBLE);
            return result = false;
        }else if(!isValidName(binding.edtName.getText().toString())){
            binding.txtErrorName.setText(getString(R.string.text_name_not_valid));
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
        }else if(!accountNumberChecking(binding.edtNumber.getText().toString(),binding.edtNumber, binding.txtErrorNumber)){
            return result = false;
        }else{
            binding.txtErrorNumber.setVisibility(View.GONE);
            result = true;
        }

        if(binding.edtIfsc.getText().toString().length() == 0){
            binding.txtErrorIFSC.setText(getString(R.string.required));
            binding.txtErrorIFSC.setVisibility(View.VISIBLE);
            return result = false;
        }else if(!isValidIFSC(binding.edtIfsc.getText().toString())){
            binding.txtErrorIFSC.setText(getString(R.string.text_ifsc_not_valid));
            binding.txtErrorIFSC.setVisibility(View.VISIBLE);
            return result = false;
        }else{
            binding.txtErrorIFSC.setVisibility(View.GONE);
            result = true;
        }

        return result;
    }

    private boolean accountNumberChecking(String toString, EditText edtNumber, MontserratLight txtErrorNumber) {
        if (TextUtils.isDigitsOnly(toString)){
            txtErrorNumber.setVisibility(View.GONE);
            return true;
        }
        txtErrorNumber.setText("Not a valid account number");
        txtErrorNumber.setVisibility(View.VISIBLE);
        return false;
    }

    private boolean isValidName(String text) {
        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(!Character.isAlphabetic(c) && !Character.isSpaceChar(c)){
                sb.append(c);
                return false;
            }
        }
        return true;
    }

    private boolean isValidIFSC(String text) {
        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(Character.isAlphabetic(c) || Character.isDigit(c)){
//                return true;
            }else{
                return false;
            }
        }
        return true;
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
                presenterMobile.fetchDetails();
                break;

        }
    }

    @Override
    public void BankDetails(JSONObject jsonObject) {
        Log.e("details",""+jsonObject);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONObject(Constants.STR_DATA);
                String BankVerified = jsonObject1.getString("BankVerified");
                String AccountNumber = jsonObject1.getString("AccountNumber");
                String IFSC = jsonObject1.getString("IFSC");
                String BankName = jsonObject1.getString("BankName");
                setBankStatus(BankVerified);
                if (IFSC.equalsIgnoreCase("null")){
                    binding.edtIfsc.setText("");
                }else{
                    binding.edtIfsc.setText(IFSC);
                }
                binding.edtName.setText(BankName);
                binding.edtNumber.setText(AccountNumber);

//                if(!status.equalsIgnoreCase("approved") || !MobileVerified.equalsIgnoreCase("yes")){
//                    binding.btnSave.setClickable(false);
//                }else{
//                    binding.btnSave.setClickable(true);
//                    binding.btnSave.setVisibility(View.VISIBLE);
//                }

            }
//            else
//                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnUpdateDetails(JSONObject jsonObject) {
        try {
            binding.btnSave.setEnabled(true);
            binding.btnSave.setClickable(true);
            binding.submitNote.setVisibility(View.GONE);

            binding.pBar.setVisibility(View.GONE);
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            } else{
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void KYCDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                String status = jsonObject1.getString("KYCStatus");
                String MobileVerified = jsonObject1.getString("MobileVerified");
                if (MobileVerified.equalsIgnoreCase("yes") && status.equalsIgnoreCase("approved")){
                    binding.btnSave.setClickable(true);
                    binding.btnSave.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                    binding.submitNote.setVisibility(View.GONE);
                }else{
                    binding.btnSave.setClickable(false);
                    binding.btnSave.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    binding.submitNote.setVisibility(View.VISIBLE);
                }

            }
//            else
//                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
            binding.btnSave.setClickable(false);
            binding.btnSave.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
            binding.submitNote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void KYCUploadStatus(JSONObject jsonObject) {

    }

    @Override
    public void OnFailed(Boolean b, String msg) {
        binding.pBar.setVisibility(View.GONE);
    }

    @Override
    public void OnVerificationCodeSent(JSONObject jsonObject) {

    }

    @Override
    public void OnVerificationCodeVerified(JSONObject jsonObject) {

    }

    private void setBankStatus(String bankStatus) {

        if (bankStatus == null || bankStatus.equalsIgnoreCase("null")){
            bankStatus = "pending";
        }
        switch (bankStatus){
            case "pending":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightYellow)));
                binding.edtStatus.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

            case "yes":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                binding.edtStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                break;

            case "no":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRedNew)));
                binding.edtStatus.setTextColor(getResources().getColor(R.color.colorRedNew));
                break;
        }

        binding.edtStatus.setText(bankStatus);

    }
}

