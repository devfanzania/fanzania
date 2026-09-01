package com.yorker.fanzania.views.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.yorker.fanzania.R;
import com.yorker.fanzania.applications.FanzaniaApplication;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ActivitySplashBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.Permissions;
import com.yorker.fanzania.helper.SingleShotLocationProvider;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.screens.auth.landingpage.LandingActivity;
import com.yorker.fanzania.views.screens.auth.login.LoginActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashPresenter.IMainView {

    private SplashPresenter presenter;

    @Override
    protected SplashPresenter onCreatePresenter() {
        presenter = new SplashPresenter(SplashActivity.this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, SplashPresenter presenter) {
        SplashPresenterComponent splashPresenterComponent = DaggerSplashPresenterComponent.builder()
                .presenterComponent(component)
                .splashApplicationModule(new SplashApplicationModule(SplashActivity.this))
                .build();
        splashPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        if(!Permissions.Check_FINE_LOCATION(this)) {
            //if not permisson granted so request permisson with request code
            //Permissions.Request_FINE_LOCATION(this,22);
        }else{
            getLocation(this);
        }
        printHashKey();
        presenter.checkTooltip();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            if (newToken != null)
                presenter.storeToken(newToken);

            System.out.println("device token " + presenter.getStoreToken());
        });

        new Handler().postDelayed(() -> {

            if (presenter.getUserID() != null && presenter.getUserEmail() != null) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//                startActivity(new Intent(SplashActivity.this, PlayerSelectionActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LandingActivity.class));
            }
            finish();

        }, 2000);

        if (CheckInternetConnection()) {
            presenter.getStaticURL();
        } else {
            new NoNetworkDialog(this, this, Constants.APICALL_1);
        }
    }
    // FETCH LOCATION FROM ACTIVITY AS BELOW
    public void getLocation(Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(Location loc) {
                        Log.e("locationUpdated", loc.getLatitude()+","+loc.getLongitude());
                        FanzaniaApplication.currentLocation = loc;
                        SingleShotLocationProvider.setLocationAddress(loc, SplashActivity.this);
                    }
                });
    }

    @Override
    public void RetryResponse(String type) {
        presenter.getStaticURL();
    }

    @Override
    public void getStaticURL(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                System.out.println("Static url " + jsonObject1.toString());

                presenter.saveURL(jsonObject1.getString("FAQs"), Constants.TAG_FAQ);
                presenter.saveURL(jsonObject1.getString("AboutUs"), Constants.TAG_ABOUTUS);
                presenter.saveURL(jsonObject1.getString("HowtoPlay"), Constants.TAG_HOWTOPLAY);
                presenter.saveURL(jsonObject1.getString("PrivacyNotice"), Constants.TAG_PRIVACYNOTICE);
                presenter.saveURL(jsonObject1.getString("PointRules"), Constants.TAG_POINTRULES);
                presenter.saveURL(jsonObject1.getString("TeamCompositionRules"), Constants.TAG_TEAMCOMPOSITIONRULE);
                presenter.saveURL(jsonObject1.getString("TnC"), Constants.TAG_TNC);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("FB", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            Log.e("FB", "printHashKey()", e);
        }
    }
}
