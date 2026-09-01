package com.yorker.fanzania.views.screens.tournament.kyc.pan;

import static com.yorker.fanzania.helper.Permissions.Check_STORAGE;
import static com.yorker.fanzania.helper.Permissions.Request_STORAGE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.FragmentUploadPanBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.CompressImage;
import com.yorker.fanzania.helper.ImageFilePath;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UploadPanFragment extends BaseFragment<UploadPanFragmentPresenter>
        implements UploadPanFragmentPresenter.IMainView, DatePickerDialog.OnDateSetListener {

    private UploadPanFragmentPresenter presenter;
    private FragmentUploadPanBinding binding;
    private String strDOB = "", state = "";
    private List<String> stateList = new ArrayList<>();
    private String imgPath = "";

    @Override
    protected UploadPanFragmentPresenter onCreatePresenter() {
        presenter = new UploadPanFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, UploadPanFragmentPresenter presenter) {
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
                inflater, R.layout.fragment_upload_pan, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListners();
        getData();
        stateList.add(0, "Select State");
        stateList.add(1, "Maharashtra");
        stateList.add(1, "Hyderabad");
        //getCountrySpinner();
    }

//    private void getCountrySpinner() {
//
//        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), stateList);
//        binding.spinner.setAdapter(customSpinnerAdapter);
//        if (state.length() > 1) {
//            int i = 0;
//            for(String entry  : stateList){
//                if (entry.equalsIgnoreCase(state)){
//                    binding.spinner.setSelection(i);
//                }
//                i ++;
//            }
//        }
//        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (!stateList.get(position).equalsIgnoreCase("Select State")) {
//                    state = stateList.get(position);
//                    binding.edtCountry.setText(stateList.get(position));
//                }else{
//                    state = "";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }

    private void getData() {
        if (CheckInternetConnection())
            presenter.fetchDetails();
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_1);
    }

    private void updateData() {
        if (CheckInternetConnection()) {
            binding.pBar.setVisibility(View.VISIBLE);
            presenter.updateKYCDetails(binding.edtName.getText().toString(), binding.edtNumber.getText().toString(), strDOB, state);
        } else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_2);
    }

    private void uploadProfileImage() {
        binding.pBar.setVisibility(View.VISIBLE);
        if (CheckInternetConnection())
            presenter.uploadImage(imgPath);
        else
            new NoNetworkDialog(getContext(), this, Constants.APICALL_3);
    }

    private void pickImageFromGallery() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        startActivityForResult(intent, Constants.PICK_IMAGE);
    }

    private void OpenDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void initListners() {

        binding.edtDOB.setOnClickListener(view -> OpenDatePicker());

        binding.llKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Check_STORAGE(getActivity())) {
                    pickImageFromGallery();
                } else {
                    Request_STORAGE(getActivity(), 120);
                }

            }
        });

//        binding.llKYC.setOnClickListener(view -> new CMDialogBuilder.showCameraOptions(getContext()) {
//            @Override
//            public void onCameraInvoked() {
//                super.onCameraInvoked();
////                uplodphoto.setEnabled(true);
//            }
//        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    binding.btnSave.setEnabled(false);
                    binding.btnSave.setClickable(false);
                    updateData();
                }
            }
        });

        binding.uploadrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPath.length() > 0)
                    uploadProfileImage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File finalFile;
        if (requestCode == Constants.PICK_IMAGE && resultCode == -1 && data != null) {
            //binding.uploadrl.setVisibility(View.VISIBLE);
            Uri selectedImageURI = data.getData();
            try {
                Log.d("sdsdsadsaadada", "dsdsd s1 " + selectedImageURI + "sdsds ::: " + ImageFilePath.getPath(getContext(), selectedImageURI));
                finalFile = new File(Objects.requireNonNull(ImageFilePath.getPath(getContext(), selectedImageURI)));
                Log.d("sdsdsadsaadada", "dsdsd s1 " + finalFile);
                CompressImage compressImage = new CompressImage(getContext());
                finalFile = new File(compressImage.compressImage(finalFile.toString()));
                imgPath = finalFile.getAbsolutePath();

//                if (imgPath.length() > 0)
//                    uploadProfileImage();

            } catch (Exception e) {
                String message = e.getMessage();
                if (message != null) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
                binding.uploadrl.setVisibility(View.GONE);
                e.printStackTrace();
                imgPath = "";
            }
        } else {
            imgPath = "";
        }
    }

    private boolean validate() {

        boolean result = false;
        state = binding.edtState.getText().toString();
        if (binding.edtName.getText().toString().length() == 0) {
            binding.txtErrorName.setText(getString(R.string.required));
            binding.txtErrorName.setVisibility(View.VISIBLE);
            return result = false;
        } else {
            binding.txtErrorName.setVisibility(View.GONE);
            result = true;
        }
        if (binding.edtNumber.getText().toString().length() == 0) {
            binding.txtErrorNumber.setText(getString(R.string.required));
            binding.txtErrorNumber.setVisibility(View.VISIBLE);
            return result = false;
        } else {
            binding.txtErrorNumber.setVisibility(View.GONE);
            result = true;
        }
        if (strDOB.length() == 0) {
            binding.txtErrorDOB.setText(getString(R.string.required));
            binding.txtErrorDOB.setVisibility(View.VISIBLE);
            return result = false;
        } else {
            binding.txtErrorDOB.setVisibility(View.GONE);
            result = true;
        }
        if (state.length() == 0) {
            binding.txtErrorCountry.setText(getString(R.string.required));
            binding.txtErrorCountry.setVisibility(View.VISIBLE);
            return result = false;
        } else {
            binding.txtErrorCountry.setVisibility(View.GONE);
            result = true;
        }
        if (imgPath.length() > 0) {
            result = true;
            binding.txtErrorImage.setVisibility(View.GONE);
        } else {
            binding.txtErrorImage.setVisibility(View.VISIBLE);
            binding.txtErrorImage.setText(getString(R.string.required));
            result = false;
        }

        return result;
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
                updateData();
                break;

            case Constants.APICALL_3:
                uploadProfileImage();
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dt = (month + 1) + "/" + dayOfMonth + "/" + year;
        strDOB = DateUtils.getDate(dt, binding.edtDOB);
    }

    @Override
    public void KYCDetails(JSONObject jsonObject) {
        Log.e("details", "" + jsonObject);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                String status = jsonObject1.getString("KYCStatus");
                binding.edtName.setText(jsonObject1.getString("PANName"));
                binding.edtNumber.setText(jsonObject1.getString("PANNumber"));
                binding.edtDOB.setText(jsonObject1.getString("PANDOB"));
                state = jsonObject1.getString("PANState");
                strDOB = jsonObject1.getString("PANDOB");
                //getCountrySpinner();
                binding.edtState.setText(state);
                if (jsonObject1.getString("KYCStatus").equals("pending")) {
                    binding.btnSave.setEnabled(false);
                    binding.btnSave.setClickable(false);
                }
                setKYCStatus(jsonObject1.getString("KYCStatus"));

            } else
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setKYCStatus(String kycStatus) {

        if (kycStatus == null || kycStatus.equalsIgnoreCase("null")) {
            kycStatus = "pending";
        }
        switch (kycStatus) {
            case "pending":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightYellow)));
                binding.edtStatus.setTextColor(requireContext().getResources().getColor(R.color.colorWhite));
                break;

            case "submitted":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkGreen)));
                binding.edtStatus.setTextColor(requireContext().getResources().getColor(R.color.colorDarkGreen));
                break;

            case "saved":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkGreen)));
                binding.edtStatus.setTextColor(requireContext().getResources().getColor(R.color.colorDarkGreen));
                break;

            case "approved":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                binding.edtStatus.setTextColor(requireContext().getResources().getColor(R.color.colorGreen));
                break;

            case "rejected":
//                binding.edtStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRedNew)));
                binding.edtStatus.setTextColor(requireContext().getResources().getColor(R.color.colorRedNew));
                break;
        }

        binding.edtStatus.setText(kycStatus);

    }

    @Override
    public void KYCUpdateDetails(JSONObject jsonObject) {
//        binding.pBar.setVisibility(View.GONE);
//        binding.btnSave.setEnabled(true);
//        binding.btnSave.setClickable(true);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                CustomToast.getInstance(getContext()).showSmallCustomToast(getString(R.string.text_kycdetailsupdated));
                if (imgPath.length() > 0)
                    uploadProfileImage();

//                presenter.updateKYCUploadStatus(Constants.PAN_STATUS.pending);
            } else
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("update", "" + jsonObject);
    }

    @Override
    public void KYCUploadImage(JSONObject jsonObject) {
        binding.pBar.setVisibility(View.GONE);
//        binding.btnSave.setEnabled(true);
//        binding.btnSave.setClickable(true);
        imgPath = "";
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                CustomToast.getInstance(getContext()).showSmallCustomToast(getString(R.string.text_pandetailsupdated));
                presenter.updateKYCUploadStatus(Constants.PAN_STATUS.pending);
            } else
                CustomToast.getInstance(getContext()).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    public void OnFailed(Boolean b) {
        binding.pBar.setVisibility(View.GONE);
    }
}

