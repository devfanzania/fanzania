package com.yorker.fanzania.views.screens.tournament.kyc.pan;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratMedium;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPanFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context mContext;

    public UploadPanFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.mContext = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void KYCDetails(JSONObject jsonObject);
        void KYCUpdateDetails(JSONObject jsonObject);
        void KYCUploadImage(JSONObject jsonObject);
        void KYCUploadStatus(JSONObject jsonObject);
        void OnFailed(Boolean b);
    }

    public void uploadImage(String imgPath) {

        MultipartBody.Part body = null;
        File file = new File(String.valueOf(imgPath));
        System.out.println("imagepath "+imgPath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        System.out.println("imagepath1 "+reqFile.toString());
        body = MultipartBody.Part.createFormData("UploadedImage", file.getName(), reqFile);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.uploadKYCImage(Constants.RETROFIT_HEADER_TYPE, "PAN", Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(), body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        System.out.println("upload image response "+jsonObject.toString());
                        activitycallback.KYCUploadImage(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void fetchDetails() {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchKYCDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.KYCDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true);
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void updateKYCDetails(String name, String number, String dob, String state) {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_PAN_NAME, name);
        map.put(Constants.TAG_PAN_NUMBER, number);
        map.put(Constants.TAG_PAN_DOB, dob);
        map.put(Constants.TAG_PAN_STATE, state);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.updateKYCDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.KYCUpdateDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true);
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void updateKYCUploadStatus(Constants.PAN_STATUS status) {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_KYC_STATUS, status);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.updateKYCUploadStatus(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.KYCUploadStatus(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true);
                System.out.println("error1 " + call.toString());
            }
        });
    }
}
