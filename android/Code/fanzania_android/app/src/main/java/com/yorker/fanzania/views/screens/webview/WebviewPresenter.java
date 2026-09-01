package com.yorker.fanzania.views.screens.webview;

import android.content.Context;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.shared.GetKeyHash;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebviewPresenter extends PresenterStub {

    private IMainView activitycallback;

    @Inject
    SharedPrefManager sharedPrefManager;

    public WebviewPresenter(IMainView activitycallback1, Context context) {
        activitycallback = activitycallback1;
        Context mContext = context;
    }

    interface IMainView {
    }

    public String getURL(String tag) {
        return sharedPrefManager.getURL(tag);
    }

}
