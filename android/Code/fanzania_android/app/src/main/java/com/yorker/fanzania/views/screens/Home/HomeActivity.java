package com.yorker.fanzania.views.screens.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.WindowCompat;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import com.razorpay.Checkout;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.NavHeaderMainBinding;
import com.yorker.fanzania.databinding.NewActivityMainBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.CommonDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.RateDialog;
import com.yorker.fanzania.dialog.UpdateDialog;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.HomeMatchContest;
import com.yorker.fanzania.views.screens.matchcontest.fragments.leagues.McLeagueFragment;
import com.yorker.fanzania.views.screens.matchcontest.fragments.livescore.McLiveFragment;
import com.yorker.fanzania.views.screens.matchcontest.fragments.teams.McTeam;
import com.yorker.fanzania.views.screens.notification.NotificationActivity;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.HomeFragment;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.LiveScoreFragment;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.MyLeagueFragment;
import com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.MyTeamFragment;
import com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.ProfileFragment;
import com.yorker.fanzania.views.screens.auth.landingpage.LandingActivity;
import com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.model.ProfileModel;
import com.yorker.fanzania.views.screens.tournament.kyc.KYCActivity;
import com.yorker.fanzania.views.screens.tournament.subscription.SubscriptionActivity;
import com.yorker.fanzania.views.screens.tournament.wallet.WalletActivity;
import com.yorker.fanzania.views.screens.webview.WebviewActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import com.yorker.fanzania.databinding.ItemBadgeBinding;

public class HomeActivity extends BaseActivity<HomePresenter>
        implements HomePresenter.IMainView,
        NavigationView.OnNavigationItemSelectedListener,
        CommonDialog.ICommonDialog,
        RateDialog.IRateDialog,
        UpdateDialog.IUpdateDialog,
NotificationActivity.MyFragmentCallback {

    private static final String TAG = "HomeActivity";
    private NewActivityMainBinding binding;
    private HomePresenter presenter;
    private long backPressedTime = 0;
    private int tabPosition = 0;
    private AppUpdateManager mAppUpdateManager;
    private ItemBadgeBinding iBinding;
    private String referralCode;
    public static NavHeaderMainBinding drawerHeaderBinding = null;
    static Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.itHome:
                binding.inAppBar.rgSwitch.setVisibility(View.VISIBLE);
                binding.inAppBar.llText.setVisibility(View.GONE);
                SelectItem(0);
                return true;

            case R.id.itMyLeague:
                binding.inAppBar.rgSwitch.setVisibility(View.VISIBLE);
                binding.inAppBar.llText.setVisibility(View.GONE);
                SelectItem(1);
                return true;

            case R.id.itLiveScore:
                binding.inAppBar.rgSwitch.setVisibility(View.VISIBLE);
                binding.inAppBar.llText.setVisibility(View.GONE);
                SelectItem(2);
                return true;

            case R.id.itMyTeam:
                binding.inAppBar.rgSwitch.setVisibility(View.VISIBLE);
                binding.inAppBar.llText.setVisibility(View.GONE);
                SelectItem(3);
                return true;

            case R.id.itMyProfile:
                binding.inAppBar.rgSwitch.setVisibility(View.GONE);
                binding.inAppBar.llText.setVisibility(View.VISIBLE);
                binding.inAppBar.txtHeader.setText(getString(R.string.title_mynotification));
                SelectItem(4);
                return true;
        }
        return false;
    };

    private void SelectItem(int position) {
        this.tabPosition = position;
        Fragment fragment = null;
        Log.e("data", ""+presenter.getHeaderPref()+", "+position);
        switch (position) {
            case 0:
                if (presenter.getHeaderPref() == 0)
                    fragment = new HomeMatchContest();
                else
                    fragment = new HomeFragment();
                break;

            case 1:
                if (presenter.getHeaderPref() == 0)
                    fragment = new McLeagueFragment();
                else
                    fragment = new MyLeagueFragment();
                break;

            case 2:
                if (presenter.getHeaderPref() == 0)
                    fragment = new McLiveFragment();
                else
                    fragment = new LiveScoreFragment();
                break;

            case 3:
                if (presenter.getHeaderPref() == 0)
                    fragment = new McTeam();
                else
                    fragment = new MyTeamFragment();
                break;

            case 4:
                fragment = new NotificationActivity();
                break;

            default:
                break;
        }

        FragmentManager frgManager = getSupportFragmentManager();
        frgManager.beginTransaction().replace(R.id.rrContentFrame, fragment)
                .addToBackStack(fragment.getClass().getSimpleName()).commit();
        notificationCout();
    }

    public void checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE },
                                100);
                    }else{
                        ActivityCompat
                                .requestPermissions(
                                        (Activity) context,
                                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                        100);
                    }

                }
            } else {
            }

        } else {
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[] { permission, Manifest.permission.MANAGE_EXTERNAL_STORAGE },
                                    100);
                        }else{
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[] { permission },
                                    100);
                        }

                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected HomePresenter onCreatePresenter() {
        presenter = new HomePresenter(this, HomeActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, HomePresenter presenter) {
        HomePresenterComponent loginPresenterComponent = DaggerHomePresenterComponent.builder()
                .presenterComponent(component)
                .homeApplicationModule(new HomeApplicationModule(HomeActivity.this))
                .build();
        loginPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        binding = DataBindingUtil.setContentView(this, R.layout.new_activity_main);
        context = this;
        binding.inAppBar.toolbar.setTitle("");
        setSupportActionBar(binding.inAppBar.toolbar);

        binding.inAppBar.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                binding.inAppBar.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (binding.navView.getWidth() * slideOffset);
                binding.inAppBar.container.setTranslationX(moveFactor);
            }
        };

        binding.drawerLayout.addDrawerListener(toggle);

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_white, HomeActivity.this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        toggle.setToolbarNavigationClickListener(v -> {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START))
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            else
                binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        drawerHeaderBinding = NavHeaderMainBinding.inflate(LayoutInflater.from(binding.navView.getContext()));
        binding.navView.addHeaderView(drawerHeaderBinding.getRoot());
        drawerHeaderBinding.tvUserName.setText(presenter.getUserName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_user);
        requestOptions.error(R.drawable.ic_user);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);

        String url = Constants.BASE_PROFILE_IMAGE_URL + presenter.getUserProfile();
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(url).into(drawerHeaderBinding.imgLogo);

        drawerHeaderBinding.HeaderMain.setOnClickListener(v->{
            startActivity(new Intent(this,ProfileFragment.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.inAppBar.navigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(4);
        LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
        iBinding = ItemBadgeBinding.inflate(layoutInflater, itemView, true);

        Fragment fragment = null;
        Log.e("insed","e-"+getIntent().getIntExtra(Constants.TAG_INDEX, 0));
        if (getIntent() != null) {
            switch (getIntent().getIntExtra(Constants.TAG_INDEX, 0)) {
                case 1:
                    fragment = new McLeagueFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.TAG_MATCHID, getIntent().getIntExtra(Constants.TAG_MATCHID, 0));
                    fragment.setArguments(bundle);
                    binding.inAppBar.navigation.getMenu().findItem(R.id.itMyLeague).setChecked(true);
                    binding.inAppBar.rbMatchContests.setChecked(true);
                    presenter.setHeaderPref(0);
                    Log.e("insed","e1");
                    break;
                case 2:
                    fragment = new McLiveFragment();
                    Bundle b = new Bundle();
                    b.putInt(Constants.TAG_MATCHID, getIntent().getIntExtra(Constants.TAG_MATCHID, 0));
                    fragment.setArguments(b);
                    binding.inAppBar.navigation.getMenu().findItem(R.id.itLiveScore).setChecked(true);
                    binding.inAppBar.rbMatchContests.setChecked(true);
                    presenter.setHeaderPref(0);
                    Log.e("insed","e2");
                    break;
                case 3:
                    fragment = new McTeam();
                    Bundle b1 = new Bundle();
                    b1.putInt(Constants.TAG_MATCHID, getIntent().getIntExtra(Constants.TAG_MATCHID, 0));
                    fragment.setArguments(b1);
                    binding.inAppBar.navigation.getMenu().findItem(R.id.itMyTeam).setChecked(true);
                    binding.inAppBar.rbMatchContests.setChecked(true);
                    presenter.setHeaderPref(0);
                    Log.e("insed","e3");
                    break;
                default:
                    Log.e("insed","e4");
                    if(presenter.GetCustomer_LoginPreference() !=null && presenter.GetCustomer_LoginPreference().equalsIgnoreCase("match")){
                        fragment = new HomeMatchContest();
                        binding.inAppBar.rbMatchContests.setChecked(true);
                        Log.e("insed","e5");
                        presenter.setHeaderPref(0);
                    }else if(presenter.GetCustomer_LoginPreference() !=null && presenter.GetCustomer_LoginPreference().equalsIgnoreCase("tournament")){
                        binding.inAppBar.rbTaurnament.setChecked(true);
                        fragment = new HomeFragment();
                        presenter.setHeaderPref(1);
                        Log.e("insed","e6");
                    }else if (presenter.getHeaderPref() == 0) {
                        fragment = new HomeMatchContest();
                        binding.inAppBar.rbMatchContests.setChecked(true);
                        Log.e("insed","e7");
                        presenter.setHeaderPref(0);
                    } else {
                        binding.inAppBar.rbTaurnament.setChecked(true);
                        fragment = new HomeFragment();
                        Log.e("insed","e8");
                        presenter.setHeaderPref(1);
                    }
                    break;
            }
        } else {
            Log.e("insed","e11");
            if(presenter.GetCustomer_LoginPreference() !=null && presenter.GetCustomer_LoginPreference().equalsIgnoreCase("match")){
                fragment = new HomeMatchContest();
                binding.inAppBar.rbMatchContests.setChecked(true);
                presenter.setHeaderPref(0);
            }else if(presenter.GetCustomer_LoginPreference() !=null && presenter.GetCustomer_LoginPreference().equalsIgnoreCase("tournament")){
                binding.inAppBar.rbTaurnament.setChecked(true);
                fragment = new HomeFragment();
                presenter.setHeaderPref(1);
            }else if (presenter.getHeaderPref() == 0) {
                fragment = new HomeMatchContest();
                binding.inAppBar.rbMatchContests.setChecked(true);
            } else {
                binding.inAppBar.rbTaurnament.setChecked(true);
                fragment = new HomeFragment();
            }
        }

        FragmentManager frgManager = getSupportFragmentManager();
        frgManager.beginTransaction().replace(R.id.rrContentFrame, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName()).commit();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            binding.navView.getMenu().findItem(R.id.nav_version).setTitle("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.inAppBar.rgSwitch.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbMatchContests:
                    presenter.setHeaderPref(0);
                    presenter.SaveCustomer_LoginPreference("match");
                    break;

                case R.id.rbTaurnament:
                    presenter.setHeaderPref(1);
                    presenter.SaveCustomer_LoginPreference("tournament");
                    break;
            }

            SelectItem(tabPosition);
        });

        Checkout.preload(getApplicationContext());

        //init order here

//        {
//            "amount": 50000,
//                "currency": "INR",
//                "receipt": "rcptid_11"
//        }

//        startPayment();

        checkPermissionREAD_EXTERNAL_STORAGE(this);
    }

    public static void updateProfileImage(ProfileModel profileModel){
        if (drawerHeaderBinding != null){
            RequestOptions requestOptions_ = new RequestOptions();
            requestOptions_.placeholder(R.drawable.ic_user);
            requestOptions_.error(R.drawable.ic_user);
            requestOptions_.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions_.skipMemoryCache(true);
            String url = Constants.BASE_PROFILE_IMAGE_URL + profileModel.getProfileImage();
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions_)
                    .load(url).into(drawerHeaderBinding.imgLogo);
        }
    }

    private void notificationCout() {
        if (CheckInternetConnection())
            presenter.getNotificationCount();
        else
            new NoNetworkDialog(this,this,Constants.APICALL_2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        notificationCout();
        checkUpdate();
    }

    private void checkUpdate() {
        AppUpdateManager mAppUpdateManager = AppUpdateManagerFactory.create(this);

//        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, HomeActivity.this, 100);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("", "checkForAppUpdateAvailability: something else");
            }
        });
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i("", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.itAboutUs)
            startActivity(new Intent(this, WebviewActivity.class)
                    .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_ABOUTUS));
        else if (id == R.id.itFAQ)
            startActivity(new Intent(this, WebviewActivity.class)
                    .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_FAQ));
        else if (id == R.id.itGameRule)
            startActivity(new Intent(this, WebviewActivity.class)
                    .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_HOWTOPLAY));
        else if (id == R.id.itTermsCondition)
            startActivity(new Intent(this, WebviewActivity.class)
                    .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_TNC));
        else if (id == R.id.itContactUs)
            startActivity(new Intent(this, WebviewActivity.class)
                    .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_CONTACTUS));
        else if (id == R.id.itWallet)
            startActivity(new Intent(this, WalletActivity.class));
        else if (id == R.id.itSubscription)
            startActivity(new Intent(this, SubscriptionActivity.class));
        else if (id == R.id.itKYC)
            startActivity(new Intent(this, KYCActivity.class));
        else if (id == R.id.itSignOut)
            new Handler().postDelayed(() -> new CommonDialog(this, getString(R.string.text_logout), getString(R.string.text_signouttext),
                    this), 150);
        else if (id == R.id.nav_share){
            if (CheckInternetConnection())
                presenter.getReferralCode();
            else
                new NoNetworkDialog(this, this, Constants.APICALL_3);
        }
        else if (id == R.id.nav_rate)
            new RateDialog(this, getString(R.string.text_rateapp), getString(R.string.text_rateuss), this);

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void PositiveResponse(Boolean value) {
        if (CheckInternetConnection())
            presenter.Logout();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.Logout();
                break;
            case Constants.APICALL_2:
                presenter.getNotificationCount();
                break;
            case Constants.APICALL_3:
                presenter.getReferralCode();
                break;
        }
    }

    @Override
    public void LogutResponse(boolean b, String statusMessage) {
        if (b) {
            CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_logouttext));
            presenter.ClearUserDetails();
            startActivity(new Intent(this, LandingActivity.class));
            finish();
        } else
            CustomToast.getInstance(this).showSmallCustomToast(statusMessage);
    }

    @Override
    public void getNotificationCount(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                int val=jsonObject.getJSONArray(Constants.STR_DATA)
                        .getJSONObject(0).getInt("NotificationCount");

                if (val>0){
                    iBinding.notifications.setVisibility(View.VISIBLE);
                    iBinding.notifications.setText(String.valueOf(val));
                }else
                    iBinding.notifications.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getReferalCode(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                int val=jsonObject.getJSONArray(Constants.STR_DATA)
                        .getJSONObject(0).getInt("NotificationCount");

                if (val>0){
                    iBinding.notifications.setVisibility(View.VISIBLE);
                    iBinding.notifications.setText(String.valueOf(val));
                }else
                    iBinding.notifications.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void showUpdateDialog() {
//        new UpdateDialog(this, getString(R.string.text_alert), getString(R.string.text_newversion), this);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 100) {
            if (resultCode != RESULT_OK) {
                Log.e("", "onActivityResult: app download failed");
            }
        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        binding.getRoot(),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorOrange));
        snackbar.show();
    }

    @Override
    public void rateUs(Boolean value) {
        presenter.rateApp();
    }

    @Override
    public void UpdateResponse(Boolean value) {
        presenter.updateApp();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {
                backPressedTime = t;
                Toast.makeText(HomeActivity.this, getString(R.string.Pressbackagaintoexit), Toast.LENGTH_SHORT).show();
            } else
                moveTaskToBack(true);
        }
    }

    @Override
    public void refreshCount() {
        notificationCout();
    }

}
