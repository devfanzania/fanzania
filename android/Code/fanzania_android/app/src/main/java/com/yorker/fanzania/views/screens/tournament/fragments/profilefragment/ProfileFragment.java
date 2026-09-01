package com.yorker.fanzania.views.screens.tournament.fragments.profilefragment;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.FragmentProfileBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.ClearGlideCacheAsyncTask;
import com.yorker.fanzania.helper.CompressImage;
import com.yorker.fanzania.helper.ImageFilePath;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.imagecapture.CMDialogBuilder;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.HomeFragment;
import com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.adapter.CustomTeamSpinnerAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.model.ProfileModel;
import com.yorker.fanzania.views.screens.auth.changepassword.ChangePasswordActivity;
import com.yorker.fanzania.views.screens.auth.registration.adapter.CustomSpinnerAdapter;
import com.yorker.fanzania.views.screens.auth.registration.model.CountryListModel;
import com.yorker.fanzania.views.screens.tournament.playerlist.PlayerListPresenter;
import com.yorker.fanzania.views.screens.tournament.playerlist.model.TeamFilterModel;
import com.yorker.fanzania.views.screens.tournament.wallet.WalletActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.widgets.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends BaseActivity<ProfileFragmentPresenter>
        implements ProfileFragmentPresenter.IMainView, PlayerListPresenter.IMainView, DatePickerDialog.OnDateSetListener {

    private ProfileFragmentPresenter presenter;
    private PlayerListPresenter playerListPresenter;
    private FragmentProfileBinding binding;
    private String strCountry = "";
    private String strTeam = "";
    private String strDOB = "";
    private ProfileModel data;
    private LinkedList<CountryListModel> countrylist;
    private String imgPath = "";
    private boolean preference = false;
    private LinkedList<TeamFilterModel> tList = new LinkedList<>();

    @Override
    protected ProfileFragmentPresenter onCreatePresenter() {
        presenter = new ProfileFragmentPresenter(this, this);
        playerListPresenter = new PlayerListPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, ProfileFragmentPresenter presenter) {
        ProfileFragmentPresenterComponent profileFragmentPresenterComponent = DaggerProfileFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .profileFragmentApplicationModule(new ProfileFragmentApplicationModule(this))
                .build();
        profileFragmentPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.fragment_profile);

        new ClearGlideCacheAsyncTask(getApplicationContext()).execute();

        initView();

        initListner();

        getCountryList();

        getData();

        binding.edtDOB.setOnClickListener(view -> OpenDatePicker());

        binding.btnSave.setOnClickListener(view -> ChechValidation());
    }


    private void initListner() {
        binding.imgProfile.setOnClickListener(view -> new CMDialogBuilder.showCameraOptions(this) {
            @Override
            public void onCameraInvoked() {
                super.onCameraInvoked();
//                uplodphoto.setEnabled(true);
            }
        });

        binding.scCpreference.setOnCheckedChangeListener((buttonView, isChecked) -> preference = isChecked);

        binding.tvReferralCode.setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Hoyeche", data.getReferralCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this,getString(R.string.text_copiedtoclipboard),Toast.LENGTH_SHORT).show();
        });

        binding.llKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });
    }

    private void pickImageFromGallery(){
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        startActivityForResult(intent,Constants.PICK_IMAGE);
    }

    private void ChechValidation() {
        presenter.NameChecking(binding.edtName.getText().toString(), binding.edtName, binding.txtErrorName);
    }

    private void initView() {

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_profile));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());


        binding.btnSave.setText(getString(R.string.text_save));
    }

    private void getData() {
        if (CheckInternetConnection())
            presenter.fetchProfile();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    private void getCountryList() {
        if (CheckInternetConnection())
            presenter.CountryList();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void getTeamList() {
        String tournamentId = HomeFragment.tournamentID != null ? HomeFragment.tournamentID : "0";

        if (CheckInternetConnection())
            playerListPresenter.teamFilter(tournamentId);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);

        MenuItem item_top = menu.findItem(R.id.action_item_one);
        MenuItem item = menu.findItem(R.id.action_item_two);

        MontserratRegular tvEdit = item_top.getActionView().findViewById(R.id.tvActionApply);
        tvEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_lock, 0, 0);
        tvEdit.setText(getString(R.string.text_change));
        tvEdit.setOnClickListener(view -> startActivity(new Intent(this, ChangePasswordActivity.class)));

//        MontserratRegular tvWallet = item.getActionView().findViewById(R.id.tvActionApply);
//        tvWallet.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_walletnew_svg, 0, 0);
//        tvWallet.setText(getString(R.string.text_wallet));
//        tvWallet.setOnClickListener(view -> startActivity(new Intent(this, WalletActivity.class)));

        if (presenter.getAuthMode() != null && presenter.getAuthMode().length() > 0)
            item_top.setVisible(false);
        return true;
    }

    private void OpenDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.CountryList();
                break;

            case Constants.APICALL_2:
                presenter.fetchProfile();
                break;

            case Constants.APICALL_3:
                updateUserInfo();
                break;

            case Constants.APICALL_4:
                uploadProfileImage();
                break;

            case Constants.APICALL_5:
                getTeamList();
                break;
        }
    }

    @Override
    public void CountryListResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                countrylist = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<CountryListModel>>() {
                                }.getType())
                );

                CountryListModel obj = new CountryListModel("0", getString(R.string.text_selectcountry), false, false);
                countrylist.add(0, obj);

                if (countrylist.size() > 0)
                    getCountrySpinner();
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateProfileImage(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                presenter.fetchProfile();

            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchProfileDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                data = presenter.getProfileData(jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0).toString());
                setUserData();
                HomeActivity.updateProfileImage(data);
                getTeamList();

            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Namevalidate(boolean b) {
        if (b)
            updateUserInfo();
    }

    private void updateUserInfo() {
        if (CheckInternetConnection()) {
            if (!binding.edtPhone.getText().toString().trim().isEmpty()) {
                if (presenter.PhoneChecking(binding.edtPhone.getText().toString(), binding.edtPhone, binding.txtErrorPhone))
                    presenter.updateProfile(binding.edtName.getText().toString(), strDOB, strCountry, binding.edtPhone.getText().toString(), preference, strTeam);
            } else
                presenter.updateProfile(binding.edtName.getText().toString(), strDOB, strCountry, "", preference, strTeam);
        } else
            new NoNetworkDialog(this, this, Constants.APICALL_3);
    }

    @Override
    public void updateProfileDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                presenter.SaveAuthToken(jsonObject1.getString(Constants.TAG_SESSIONID));

                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_profiledetailsupdated));
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserData() {
        binding.pBar.setVisibility(View.GONE);
        binding.llMain.setVisibility(View.VISIBLE);
        binding.edtName.setText(data.getName());
        binding.edtEmail.setText(data.getEmail());

        if (data.getPhoneNumber() != null)
            binding.edtPhone.setText(data.getPhoneNumber());

        if (data.getCountry() != null) {
            binding.edtCountry.setText(data.getCountry());
            strCountry = data.getCountryId();
        }

        if (data.getBackgroundTheme() != null) {
            strTeam = data.getBackgroundTheme();
        }

        if (data.getDOB() != null)
            strDOB = DateUtils.getDate(data.getDOB(),binding.edtDOB);

        preference = data.isCommPreference();

        if (preference)
            binding.scCpreference.setChecked(true);
        else
            binding.scCpreference.setChecked(false);

        if (data.getProfileImage()!=null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_user);
            requestOptions.error(R.drawable.ic_user);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            String url = Constants.BASE_PROFILE_IMAGE_URL + data.getProfileImage();

            Glide.get(this).clearMemory();
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgProfile);
        }

        String txt=getString(R.string.text_referralcode)+" <font color=#081B39><b>"+data.getReferralCode()+"</b></font>";
        binding.tvReferralCode.setText(Html.fromHtml(txt));

        String txt1=getString(R.string.text_referralcount)+" <font color=#081B39><b>"+data.getReferralCount()+"</b></font>";
        binding.tvReferralCount.setText(Html.fromHtml(txt1));

        saveToSharedPref();
    }

    private void saveToSharedPref(){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        sharedPrefManager.saveCustomer_Phone(data.getPhoneNumber());
        sharedPrefManager.saveCustomerName(data.getName());

    }

    private void getCountrySpinner() {

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, countrylist);
        binding.spinner.setAdapter(customSpinnerAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!countrylist.get(position).getCountryId().equals("0")) {
                    strCountry = countrylist.get(position).getCountryId();
                    binding.edtCountry.setText(countrylist.get(position).getCountry());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File finalFile;
        if (requestCode == Constants.ACTION_TAKE_CAMERA && resultCode == 1 && data != null) {

            finalFile = new File(data.getStringExtra("data"));
            CompressImage compressImage = new CompressImage(this);
            finalFile = new File(compressImage.compressImage(finalFile.toString()));
            imgPath = finalFile.getAbsolutePath();

            if (imgPath.length() > 0)
                uploadProfileImage();

        } else if (requestCode == Constants.ACTION_TAKE_GALLERY && resultCode == -1 && data != null) {
            Uri selectedImageURI = data.getData();
            try {
                finalFile = new File(ImageFilePath.getPath(this, selectedImageURI));
                CompressImage compressImage = new CompressImage(this);
                finalFile = new File(compressImage.compressImage(finalFile.toString()));
                imgPath = finalFile.getAbsolutePath();

                if (imgPath.length() > 0)
                    uploadProfileImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImage() {

        if (CheckInternetConnection())
            presenter.uploadImage(imgPath);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_4);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dt = (month + 1) + "/" + dayOfMonth + "/" + year;
        strDOB=DateUtils.getDate(dt,binding.edtDOB);
    }

    @Override
    public void getTournamentMatchList(JSONObject jsonObject) {

    }

    @Override
    public void getPlayerlist(JSONObject jsonObject) {

    }

    @Override
    public void getFilterTeamList(JSONObject jsonObject) {

        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                String data = "[\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 51,\n" +
                        "           \"ParticipationTeamName\": \"Chennai Super Kings\",\n" +
                        "           \"TeamShortName\": \"CSK\",\n" +
                        "           \"TeamImage\": \"CSK.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 50,\n" +
                        "           \"ParticipationTeamName\": \"Delhi Capitals\",\n" +
                        "           \"TeamShortName\": \"DC\",\n" +
                        "           \"TeamImage\": \"DC.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 49,\n" +
                        "           \"ParticipationTeamName\": \"Kolkata Knight Riders\",\n" +
                        "           \"TeamShortName\": \"KKR\",\n" +
                        "           \"TeamImage\": \"KKR.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 53,\n" +
                        "           \"ParticipationTeamName\": \"Kings XI Punjab\",\n" +
                        "           \"TeamShortName\": \"KXIP\",\n" +
                        "           \"TeamImage\": \"KXIP.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 48,\n" +
                        "           \"ParticipationTeamName\": \"Mumbai Indians\",\n" +
                        "           \"TeamShortName\": \"MI\",\n" +
                        "           \"TeamImage\": \"MI.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 47,\n" +
                        "           \"ParticipationTeamName\": \"Royal Challengers Bangalore\",\n" +
                        "           \"TeamShortName\": \"RCB\",\n" +
                        "           \"TeamImage\": \"RCB.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 52,\n" +
                        "           \"ParticipationTeamName\": \"Rajasthan Royals\",\n" +
                        "           \"TeamShortName\": \"RR\",\n" +
                        "           \"TeamImage\": \"RR.png\"\n" +
                        "       },\n" +
                        "       {\n" +
                        "           \"TournamentId\": 101,\n" +
                        "           \"ParticipationTeamId\": 54,\n" +
                        "           \"ParticipationTeamName\": \"Sunrisers Hyderabad\",\n" +
                        "           \"TeamShortName\": \"SRH\",\n" +
                        "           \"TeamImage\": \"SRH.png\"\n" +
                        "       }\n" +
                        "   ]";
                tList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
//                                data
                                , new TypeToken<List<TeamFilterModel>>() {
                                }.getType())
                );

                TeamFilterModel obj = new TeamFilterModel();
                obj.setTeamShortName(getString(R.string.text_select_team));
                obj.setTournamentId(0);
                tList.add(0, obj);

                if (tList.size() > 0)
                    getTeamSpinner();

            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getTeamSpinner() {

        CustomTeamSpinnerAdapter customSpinnerAdapter = new CustomTeamSpinnerAdapter(this, tList);
        binding.spinnerTeam.setAdapter(customSpinnerAdapter);
        if (strTeam.length() > 1) {
            int i = 0;
            for(TeamFilterModel entry  : tList){
                if (entry.getTeamShortName().equals(strTeam)){
                    binding.spinnerTeam.setSelection(i);
                }
                i ++;
            }
        }

        binding.spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (tList.get(position).getTournamentId() != 0) {
                    strTeam = String.valueOf(tList.get(position).getTeamShortName());
                    binding.edtTeam.setText(tList.get(position).getTeamShortName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void getTeamRuleDetails(JSONObject jsonObject) {

    }

    @Override
    public void getPlayerDetails(JSONObject jsonObject) {

    }
}
