package com.yorker.fanzania.views.screens.auth.changepassword;

import android.content.Context;
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

public class ChangePasswordPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    Validation validation;

    @Inject
    SharedPrefManager sharedPrefManager;

    public ChangePasswordPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {
//        void CurrentPasswordvalidate(Boolean value);

        void NewPasswordvalidate(Boolean value);

        void ConfirmPasswordValidate(Boolean value);

        void ResetEmail(JSONObject value);
    }

//    public String getCustomerPassword() {
//        return sharedPrefManager.getCustomer_Password();
//    }

    public String getCustomerID() {
        return sharedPrefManager.getCustomer_Id();
    }

    public void setCustomerPassword(String password) {
        sharedPrefManager.saveCustomer_Password(password);
    }

//    public void CurrentPassword(String currentpassword, EditText field, TextView txtErrorCPass) {
//
//        if (!currentpassword.trim().isEmpty()) {
//            if (currentpassword.equals(getCustomerPassword())) {
//                txtErrorCPass.setText("");
//                txtErrorCPass.setVisibility(View.GONE);
//                activitycallback.CurrentPasswordvalidate(true);
//            } else {
//                txtErrorCPass.setText(context.getString(R.string.text_currentpassnotmatch));
//                activitycallback.CurrentPasswordvalidate(false);
//                txtErrorCPass.setVisibility(View.VISIBLE);
//            }
//        } else {
//            txtErrorCPass.setText(context.getString(R.string.text_placeisblank));
//            activitycallback.CurrentPasswordvalidate(false);
//            txtErrorCPass.setVisibility(View.VISIBLE);
//        }
//    }


    public void ValidatePassword(String str_password, EditText password, TextView txtError) {

        if (str_password != null && !str_password.trim().isEmpty()) {
            if (validation.PasswordValidation(str_password)) {
                activitycallback.NewPasswordvalidate(true);
                txtError.setText("");
                txtError.setVisibility(View.GONE);
            } else {
                activitycallback.NewPasswordvalidate(false);
                password.requestFocus();
                txtError.setText(context.getString(R.string.text_password_must_have));
                txtError.setVisibility(View.VISIBLE);
            }
        } else {
            activitycallback.NewPasswordvalidate(false);
            password.requestFocus();
            txtError.setText(context.getString(R.string.text_password_field_is_blank));
            txtError.setVisibility(View.VISIBLE);
        }
    }

    public void ConfirmPassword(String strcnfpass, EditText filed, TextView txt, String strpass) {

        if (validation.StringChecking(strcnfpass)) {
            if (strcnfpass.equals(strpass)) {
                activitycallback.ConfirmPasswordValidate(true);
                txt.setText(null);
                txt.setVisibility(View.GONE);
            } else {
                activitycallback.ConfirmPasswordValidate(false);
                filed.requestFocus();
                txt.setVisibility(View.VISIBLE);
                txt.setText(context.getString(R.string.text_passwordnotmatched));
            }
        } else {
            activitycallback.ConfirmPasswordValidate(false);
            filed.requestFocus();
            txt.setVisibility(View.VISIBLE);
            txt.setText(context.getString(R.string.text_placeisblank));
        }
    }

    public void ChangePassword(String password) {

        Map<String, Object> map = new HashMap<>();
        map.put("UserId", getCustomerID());
        map.put("Password", password);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.ChangePassword(Constants.str_HEADER, Constants.RETROFIT_HEADER_TYPE,Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.ResetEmail(jsonObject);
                        else
                            activitycallback.ResetEmail(jsonObject);

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

}
