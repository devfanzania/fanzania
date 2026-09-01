package com.yorker.fanzania.views.screens.auth.forgotpassword;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
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

public class ForgotPasswordPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    Validation validation;

    public ForgotPasswordPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public void validateEmail(String email, EditText loginEmail, TextView txtError) {
        if (email != null && !email.trim().isEmpty()) {
            switch (validation.Emailvalidation(email)) {
                case 0:
                    activitycallback.Emailvalidate(true);
                    loginEmail.setError(null);
                    txtError.setText("");
                    break;

                case 1:
                    activitycallback.Emailvalidate(false);
                    loginEmail.requestFocus();
                    txtError.setText(context.getString(R.string.text_email_not_valid));
                    break;

                case 2:
                    activitycallback.Emailvalidate(false);
                    loginEmail.requestFocus();
                    txtError.setText(context.getString(R.string.text_email_is_blank));
                    break;
            }
        } else {
            activitycallback.Emailvalidate(false);
            loginEmail.requestFocus();
            txtError.setText(context.getString(R.string.text_email_is_blank));
        }
    }

    public void EmailChecking(String email) {

        Map<String, Object> map = new HashMap<>();
        map.put("Email", email);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.EmailChecking(Constants.str_HEADER, Constants.RETROFIT_HEADER_TYPE,Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.EmailExistsChecking(true);
                        else
                            activitycallback.EmailExistsChecking(false);

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

    public void ForgotPassword(String email) {

        Map<String, Object> map = new HashMap<>();
        map.put("Email", email);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.forgotPassword(Constants.str_HEADER, Constants.RETROFIT_HEADER_TYPE,Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String txt = jsonObject.getString("statusMessage");

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.ForgotPasswordResponse(true, txt);
                        else
                            activitycallback.ForgotPasswordResponse(false, txt);

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

    public interface IMainView {
        void Emailvalidate(boolean b);

        void EmailExistsChecking(boolean b);

        void ForgotPasswordResponse(boolean b, String txt);
    }

}
