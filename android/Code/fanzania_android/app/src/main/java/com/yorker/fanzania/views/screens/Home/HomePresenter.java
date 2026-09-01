package com.yorker.fanzania.views.screens.Home;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;
//    private String currentVersion;

    public HomePresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {

        void LogutResponse(boolean b, String statusMessage);
        void getNotificationCount(JSONObject obj);
        void getReferalCode(JSONObject obj);

//        void showUpdateDialog();
    }

    public void Logout() {
        Map<String, Object> map = new HashMap<>();
        map.put("UserId", sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.Logout(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, sharedPrefManager.getAuthToken(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.LogutResponse(true, jsonObject.getString("statusMessage"));
                        else
                            activitycallback.LogutResponse(false, jsonObject.getString("statusMessage"));
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

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            context.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            context.startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }

    public void ClearUserDetails() {
        sharedPrefManager.claerCustomerData();
    }

    public String getUserName() {
        return sharedPrefManager.getCustomerName();
    }

    public String getUserProfile() {
        return sharedPrefManager.getCustomer_Profile();
    }

    public void checkVersion() {

//        try {
//            currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
//            new GetVersionCode().execute();
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public void updateApp() {
        String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity  object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

//    private class GetVersionCode extends AsyncTask<Void, String, String> {
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            String newVersion = null;
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=it")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select(".hAyfc .htlgb")
//                        .get(7)
//                        .ownText();
//                return newVersion;
//            } catch (Exception e) {
//                return newVersion;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String onlineVersion) {
//            super.onPostExecute(onlineVersion);
//
//            if (onlineVersion != null && !onlineVersion.isEmpty()) {
//                if (!currentVersion.equals(onlineVersion))
//                        activitycallback.showUpdateDialog();
//            }
//        }
//    }

    public void setHeaderPref(int val){
        sharedPrefManager.setHeaderPref(val);
    }

    public int getHeaderPref(){
        return sharedPrefManager.getHeaderPref();
    }

    public void SaveCustomer_LoginPreference(String val){
        sharedPrefManager.saveCustomer_LoginPreference(val);
        updateLoginPreference(val);
    }

    public String GetCustomer_LoginPreference() {
        return sharedPrefManager.getCustomer_LoginPreference();
    }

    public void updateLoginPreference(String preference) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));
        map.put(Constants.TAG_LOGIN_PREFERENCE, preference);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.saveLoginPreference(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

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

    public void getNotificationCount() {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchNotificationCount(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.getReferalCode(jsonObject);

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

    public void getReferralCode() {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchReferalCode(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        try {
                            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                                String val=jsonObject.getJSONArray(Constants.STR_DATA)
                                        .getJSONObject(0).getString("ReferralCode");
                                shareApp(val);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    public void shareApp(String val) {
        final String appPackageName = context.getPackageName();

        StringBuilder str=new StringBuilder().append(context.getString(R.string.text_appshare)).append("https://play.google.com/store/apps/details?id=")
                .append(appPackageName).append(context.getString(R.string.text_appshare4)).append(" ").append(val).append(context.getString(R.string.text_appshare3));
//        String text = context.getString(R.string.text_appshare) + "https://play.google.com/store/apps/details?id="
//                + appPackageName + context.getString(R.string.text_appshare4+val+ context.getString(R.string.text_appshare3);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str.toString());
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

}
