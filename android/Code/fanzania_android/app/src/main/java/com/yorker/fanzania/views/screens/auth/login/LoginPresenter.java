package com.yorker.fanzania.views.screens.auth.login;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.applications.FanzaniaApplication;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.shared.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    Validation validation;

    @Inject
    SharedPrefManager sharedPrefManager;


    public LoginPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public void validateEmail(String email, EditText loginEmail, TextView txtError) {
        boolean b = false;
        if (email != null && !email.trim().isEmpty()) {
            switch (validation.Emailvalidation(email)) {
                case 0:
                    b = true;
                    loginEmail.setError(null);
                    txtError.setText("");
                    break;

                case 1:
                    b = false;
                    loginEmail.requestFocus();
                    txtError.setText(context.getString(R.string.text_email_not_valid));
                    break;

                case 2:
                    b = false;
                    loginEmail.requestFocus();
                    txtError.setText(context.getString(R.string.text_email_is_blank));
                    break;
            }
        } else {
            b = false;
            loginEmail.requestFocus();
            txtError.setText(context.getString(R.string.text_email_is_blank));
        }

        activitycallback.Emailvalidate(b);
    }

    public interface IMainView {
        void Emailvalidate(boolean b);

        void LoginResponse(JSONObject jsonObject);

        void SocialRegistrationResponse(JSONObject jsonObject);
    }

    public void Login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("Email", username);
        map.put("Password", password);
        if (FanzaniaApplication.address != null)
            map.put("LoginLocation", FanzaniaApplication.address);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.Login(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.LoginResponse(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + t.toString());
            }
        });
    }

    public void SocialRegistration(String username, String LoginProviderAccessToken, String LoginProvider, String Email, String Name) {
        Map<String, Object> map = new HashMap<>();
        map.put("UserName", username);
        map.put("LoginProviderAccessToken", LoginProviderAccessToken);
        map.put("LoginProvider", LoginProvider);
        map.put("Email", Email);
        map.put("Name", Name);
        if (FanzaniaApplication.address != null)
            map.put("LoginLocation", FanzaniaApplication.address);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.SocialRegistration(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.SocialRegistrationResponse(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void SaveCustomer_id(String Customer_id) {
        sharedPrefManager.saveCustomer_Id(Customer_id);
    }

    public void SaveCustomerName(String name) {
        sharedPrefManager.saveCustomerName(name);
    }

    public void SaveCustomer_Email(String email) {
        sharedPrefManager.saveCustomer_Email(email);
    }

    public void SaveCustomer_Profile(String profileImage) {
        sharedPrefManager.saveCustomer_Profile(profileImage);
    }

    public void SaveCustomer_Password(String password) {
        sharedPrefManager.saveCustomer_Password(password);
    }

    public void SaveCustomer_Phone(String password) {
        sharedPrefManager.saveCustomer_Phone(password);
    }

    public void SaveAuthToken(String token) {
        sharedPrefManager.saveAuthToken(token);
    }

    public void SaveConnectionID(String connectionID) {
        sharedPrefManager.saveCustomer_COnnectionID(connectionID);
    }

    public void saveAuthMode(String mode) {
        sharedPrefManager.saveAuthMode(mode);
    }

    public void SaveCustomer_LoginPreference(String preference) {
        sharedPrefManager.saveCustomer_LoginPreference(preference);
    }


}
