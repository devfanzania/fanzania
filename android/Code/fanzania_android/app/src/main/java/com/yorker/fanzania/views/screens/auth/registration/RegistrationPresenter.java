package com.yorker.fanzania.views.screens.auth.registration;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
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

public class RegistrationPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;
    private GradientDrawable bgShape;

    public RegistrationPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    @Inject
    Validation validation;

    @Inject
    SharedPrefManager sharedPrefManager;

    public void validateEmail(String email, EditText loginEmail, TextView txtError) {

        bgShape = (GradientDrawable) loginEmail.getBackground();

        if (email != null && !email.trim().isEmpty()) {
            switch (validation.Emailvalidation(email)) {
                case 0:
                    activitycallback.Emailvalidate(true);
                    loginEmail.setError(null);
                    txtError.setText("");
                    txtError.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorWhite, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorWhite));

                    break;

                case 1:
                    activitycallback.Emailvalidate(false);
                    loginEmail.requestFocus();
                    txtError.setText(context.getString(R.string.text_email_not_valid));
                    txtError.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));
                    break;

                case 2:
                    activitycallback.Emailvalidate(false);
                    loginEmail.requestFocus();
                    txtError.setText(context.getString(R.string.text_email_is_blank));
                    txtError.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));

                    break;
            }
        } else {
            activitycallback.Emailvalidate(false);
            loginEmail.requestFocus();
            txtError.setText(context.getString(R.string.text_email_is_blank));
            txtError.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
            else
                bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));
        }
    }

    public void CheckEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            switch (validation.Emailvalidation(email)) {
                case 0:
                    activitycallback.CheckEmail(true);
                    break;
            }
        }
    }

    public void NameChecking(String str_fname, EditText filed, TextView txt) {

        bgShape = (GradientDrawable) filed.getBackground();

        if (validation.StringChecking(str_fname)) {
            activitycallback.Namevalidate(true);
            txt.setText("");
            txt.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                bgShape.setStroke(1, context.getResources().getColor(R.color.colorWhite, context.getTheme()));
            else
                bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorWhite));

        } else {
            activitycallback.Namevalidate(false);
            filed.requestFocus();
            txt.setText(context.getString(R.string.text_fnmae_required));
            txt.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
            else
                bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));
        }
    }

    public void ValidatePassword(String str_password, EditText password, TextView txtError) {
        if (str_password != null && !str_password.trim().isEmpty()) {
            if (validation.PasswordValidation(str_password)) {
                activitycallback.Passwordvalidate(true);
                txtError.setText("");
                txtError.setVisibility(View.GONE);
            } else {
                activitycallback.Passwordvalidate(false);
                password.requestFocus();
                txtError.setText(context.getString(R.string.text_password_must_have));
                txtError.setVisibility(View.VISIBLE);
            }
        } else {
            activitycallback.Passwordvalidate(false);
            password.requestFocus();
            txtError.setText(context.getString(R.string.text_password_field_is_blank));
            txtError.setVisibility(View.VISIBLE);
        }
    }

    public interface IMainView {
        void EmailChecking(boolean b);

        void CheckEmail(boolean b);

        void Emailvalidate(boolean b);

        void Passwordvalidate(boolean b);

        void Namevalidate(boolean b);

        void RegistrationResponse(JSONObject jsonObject);
    }

    public void EmailChecking(String email) {

        Map<String, Object> map = new HashMap<>();
        map.put("Email", email);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.EmailChecking(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.EmailChecking(true);
                        else
                            activitycallback.EmailChecking(false);

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

    public void Registration(String email, String password, String name,String referralcode) {

        Map<String, Object> map = new HashMap<>();
        map.put("Email", email);
        map.put("Password", password);
        map.put("Name", name);
        map.put("ReferralCodeUsed", referralcode);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.Registration(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.RegistrationResponse(jsonObject);
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

    public void SaveCustomer_Password(String password) {
        sharedPrefManager.saveCustomer_Password(password);
    }
}
