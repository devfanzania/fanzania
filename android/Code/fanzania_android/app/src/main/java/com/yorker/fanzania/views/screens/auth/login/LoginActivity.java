package com.yorker.fanzania.views.screens.auth.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.yorker.fanzania.R;
import com.yorker.fanzania.applications.FanzaniaApplication;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityLoginNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.Permissions;
import com.yorker.fanzania.helper.SingleShotLocationProvider;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.screens.auth.emailverification.EmailVerificationActivity;
import com.yorker.fanzania.views.screens.auth.forgotpassword.ForgotPasswordActivity;
import com.yorker.fanzania.views.screens.auth.registration.RegistrationActivity;
import com.yorker.fanzania.views.screens.webview.WebviewActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginPresenter.IMainView {

    private LoginPresenter presenter;
    private ActivityLoginNewBinding binding;
    private Boolean isEmailValid = false;
    private Boolean isPasswordValid = false;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private String social_mode;
    private String connectionID = "";
    private String name = "";
    private String email = "";
    private ProgressDialog pd;

    @Override
    protected LoginPresenter onCreatePresenter() {
        presenter = new LoginPresenter(this, LoginActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LoginPresenter presenter) {
        LoginPresenterComponent loginPresenterComponent = DaggerLoginPresenterComponent.builder()
                .presenterComponent(component)
                .loginApplicationModule(new LoginApplicationModule(LoginActivity.this))
                .build();
        loginPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_new);

        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.text_loading));

        setViews();
        initializeFB();
        setupGoogleSignIn();

        if(!Permissions.Check_FINE_LOCATION(this)) {
            //if not permisson granted so request permisson with request code
            Permissions.Request_FINE_LOCATION(this,22);
        }else{
            getLocation(this);
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
                        SingleShotLocationProvider.setLocationAddress(loc, LoginActivity.this);
                    }
                });
    }

    private void initializeFB() {
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            connectionID = loginResult.getAccessToken().getUserId();
                            RequestData();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
    }

    private void setViews() {

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.toolbar.setTitle("");

        setSupportActionBar(binding.toolbar);

        if (binding.toolbar != null)
            binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnSignIn.setText(getString(R.string.text_signin));

        binding.edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    presenter.validateEmail(binding.edtEmail.getText().toString(), binding.edtEmail, binding.txtErrorEmail);
                else {
                    isEmailValid = false;
                    binding.btnSignIn.setEnabled(true);
                    binding.txtErrorEmail.setText("");
                }
            }
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    binding.txtErrorPassword.setText("");
                    isPasswordValid = true;
                    if (isEmailValid)
                        binding.btnSignIn.setEnabled(true);
                } else {
                    isPasswordValid = false;
                    binding.btnSignIn.setEnabled(false);
                }
            }
        });

        binding.btnSignIn.setOnClickListener(view -> Checkvalidation());
        binding.btnGotoSignUp.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));

        binding.txtForgotPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        binding.btnGoogle.setOnClickListener(view -> {
            social_mode = Constants.SOCIAL_MODE_GOOGLE;
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 100);
        });

        binding.btnFacebook.setOnClickListener(view -> {
            social_mode = Constants.SOCIAL_MODE_FB;
            LoginManager.getInstance().logInWithReadPermissions(Objects.requireNonNull(this),
                    Arrays.asList("public_profile", "email"));
        });

        binding.btnSupport.setOnClickListener(view->startActivity(new Intent(this, WebviewActivity.class)
                .putExtra(Constants.TAG_INTENTKEY, Constants.TAG_CONTACTUS)));
//        binding.edtEmail.setText("das.rajibdas@gmail.com");
//        binding.edtPassword.setText("12345678");
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            JSONObject json = response.getJSONObject();
            if (json != null) {

                try {
                    name = json.getString("name");
                    email = json.getString("email");

                    if (CheckInternetConnection())
                        presenter.SocialRegistration(email, connectionID, social_mode, email, name);
                    else
                        new NoNetworkDialog(this, this, Constants.APICALL_3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("fb data " + json.toString());
                LoginManager.getInstance().logOut();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void Checkvalidation() {

        if (isEmailValid) {
            if (binding.edtPassword.getText().toString().length() > 0) {
                if (CheckInternetConnection()) {
                    presenter.Login(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString());
                    binding.btnSignIn.setEnabled(false);
                    binding.btnFacebook.setEnabled(false);
                    binding.btnGoogle.setEnabled(false);
                    binding.btnGotoSignUp.setEnabled(false);
                    binding.pbSignUp.setVisibility(View.VISIBLE);
                } else
                    new NoNetworkDialog(this, this, Constants.APICALL_1);
            } else {
                isPasswordValid = false;
                binding.btnSignIn.setEnabled(false);
                binding.txtErrorPassword.setText(getString(R.string.text_email_not_valid));
            }
        } else
            presenter.validateEmail(binding.edtEmail.getText().toString(), binding.edtEmail, binding.txtErrorEmail);
    }

    @Override
    public void Emailvalidate(boolean b) {
        isEmailValid = b;
        if (isPasswordValid)
            binding.btnSignIn.setEnabled(true);
    }

    @Override
    public void LoginResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                presenter.SaveCustomer_Password(binding.edtPassword.getText().toString());

                if (jsonObject1.getBoolean("Active")) {
                    presenter.SaveCustomer_id(jsonObject1.getString(Constants.TAG_ID));

                    presenter.SaveCustomerName(jsonObject1.getString(Constants.TAG_NAME));

                    presenter.SaveCustomer_Email(jsonObject1.getString(Constants.TAG_EMAIL));
                    presenter.SaveCustomer_Profile(jsonObject1.getString(Constants.TAG_PROFILE_IMAGE));

                    presenter.SaveCustomer_Phone(jsonObject1.getString(Constants.TAG_PHONENUMBER));

                    presenter.SaveAuthToken(jsonObject1.getString(Constants.TAG_SESSIONID));
                    presenter.SaveCustomer_LoginPreference(jsonObject1.getString(Constants.TAG_LOGIN_PREFERENCE));
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_loginsuccess));

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finishAffinity();
                } else {
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_activeyouremail));

                    startActivity(new Intent(LoginActivity.this, EmailVerificationActivity.class)
                            .putExtra(Constants.TAG_USERDETAILS, jsonObject1.toString())
                            .putExtra(Constants.TAG_CLASS, 1));
                }

            } else
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_emailorpasswordnovalid));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.btnSignIn.setEnabled(true);
        binding.btnFacebook.setEnabled(true);
        binding.btnGoogle.setEnabled(true);
        binding.btnGotoSignUp.setEnabled(true);
        binding.pbSignUp.setVisibility(View.GONE);
    }

    @Override
    public void SocialRegistrationResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                presenter.SaveCustomer_id(jsonObject1.getString(Constants.TAG_ID));

                presenter.SaveCustomerName(jsonObject1.getString(Constants.TAG_NAME));

                presenter.SaveConnectionID(connectionID);

                presenter.saveAuthMode(social_mode);

                presenter.SaveCustomer_Email(jsonObject1.getString(Constants.TAG_EMAIL));
                presenter.SaveCustomer_Profile(jsonObject1.getString(Constants.TAG_PROFILE_IMAGE));

                presenter.SaveCustomer_Phone(jsonObject1.getString(Constants.TAG_PHONENUMBER));

                presenter.SaveCustomer_Password(binding.edtPassword.getText().toString());

                presenter.SaveAuthToken(jsonObject1.getString(Constants.TAG_SESSIONID));

                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_loginsuccess));

                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finishAffinity();

            } else
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_sociallogintxt));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pd.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (social_mode) {
            case Constants.SOCIAL_MODE_GOOGLE:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;

            case Constants.SOCIAL_MODE_FB:
                System.out.println("onActivity request code login " + requestCode);
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount acct) {
        if (acct != null) {
            name = acct.getDisplayName();
            email = acct.getEmail();
            connectionID = acct.getId();
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, task -> {
                        // ...
                    });
            if (CheckInternetConnection())
                presenter.SocialRegistration(email, connectionID, social_mode, email, name);
            else
                new NoNetworkDialog(this, this, Constants.APICALL_3);
        }
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {

            case Constants.APICALL_1:
                presenter.Login(binding.edtEmail.getText().toString(),
                        binding.edtPassword.getText().toString());
                binding.btnSignIn.setEnabled(false);
                binding.btnFacebook.setEnabled(false);
                binding.btnGoogle.setEnabled(false);
                binding.btnGotoSignUp.setEnabled(false);
                binding.pbSignUp.setVisibility(View.VISIBLE);
                break;

            case Constants.APICALL_3:
                presenter.SocialRegistration(email, connectionID, social_mode, email, name);
                break;

        }
    }

}
