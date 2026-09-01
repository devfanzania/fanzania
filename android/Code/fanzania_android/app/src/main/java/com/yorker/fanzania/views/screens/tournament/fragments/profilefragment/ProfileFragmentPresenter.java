package com.yorker.fanzania.views.screens.tournament.fragments.profilefragment;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratLight;
import com.yorker.fanzania.helper.GetUserData;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.model.ProfileModel;
import com.yorker.fanzania.views.shared.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    public ProfileFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    @Inject
    GetUserData getUserData;

    @Inject
    Validation validation;

    public interface IMainView {
        void CountryListResponse(JSONObject jsonObject);

        void fetchProfileDetails(JSONObject jsonObject);

        void Namevalidate(boolean b);

        void updateProfileDetails(JSONObject jsonObject);

        void updateProfileImage(JSONObject jsonObject);
    }

    public String getAuthMode() {
        return sharedPrefManager.getAuthMode();
    }

    public void CountryList() {

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.CountryList(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.CountryListResponse(jsonObject);
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

    public void fetchProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.FetchProfileDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.fetchProfileDetails(jsonObject);
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

    public void uploadImage(String imgPath) {

        MultipartBody.Part body = null;
        File file = new File(String.valueOf(imgPath));
        System.out.println("imagepath "+imgPath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        System.out.println("imagepath1 "+reqFile.toString());
        body = MultipartBody.Part.createFormData("UploadedImage", file.getName(), reqFile);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.uploadProfileImage(Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(), body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        System.out.println("upload image response "+jsonObject.toString());
                        activitycallback.updateProfileImage(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString()+", "+t.getMessage());
            }
        });
    }

    public void updateProfile(String name, String dob, String countryid, String phone, boolean value, String team) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_NAME, name);
        map.put(Constants.TAG_DOB, dob);
        map.put(Constants.TAG_COUNTRYID, countryid);
        map.put(Constants.TAG_PHONENUMBER, phone);
        map.put(Constants.TAG_PREFERENCE, value);
        map.put(Constants.TAG_BACKGROUND_THEME, team);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.UpdateProfileDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, sharedPrefManager.getAuthToken(),
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.updateProfileDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString()+", "+t.getMessage());
            }
        });
    }

    public void NameChecking(String str_fname, EditText filed, MontserratLight txt) {
        if (validation.StringChecking(str_fname)) {
            activitycallback.Namevalidate(true);
            txt.setText("");
            txt.setVisibility(View.GONE);
        } else {
            activitycallback.Namevalidate(false);
            filed.requestFocus();
            txt.setText(context.getString(R.string.text_fnmae_required));
            txt.setVisibility(View.VISIBLE);
        }
    }

    public Boolean PhoneChecking(String str_phone, EditText field, MontserratLight txt){
        if (str_phone.length()>9){
            txt.setText(null);
            txt.setVisibility(View.GONE);
            return true;
        } else {
            field.requestFocus();
            txt.setText(context.getString(R.string.text_phonenumberisnotvalid));
            txt.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public ProfileModel getProfileData(String data) {
        return getUserData.getProfileData(data);
    }

    public void SaveAuthToken(String token) {
        sharedPrefManager.saveAuthToken(token);
    }
}
