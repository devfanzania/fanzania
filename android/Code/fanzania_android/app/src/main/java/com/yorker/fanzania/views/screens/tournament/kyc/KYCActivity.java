package com.yorker.fanzania.views.screens.tournament.kyc;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ActivityKycBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.views.screens.tournament.kyc.bankdetails.BankDetailsFragment;
import com.yorker.fanzania.views.screens.tournament.kyc.emailmobile.UpdateEmailMobileFragment;
import com.yorker.fanzania.views.screens.tournament.kyc.pan.UploadPanFragment;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

public class KYCActivity extends BaseActivity<KYCPresenter> implements KYCPresenter.IMainView {

    private static final String TAG = "SubscriptionActivity";
    private KYCPresenter presenter;
    private ActivityKycBinding binding;


    @Override
    protected KYCPresenter onCreatePresenter() {
        presenter = new KYCPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, KYCPresenter presenter) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        initViews();
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_kyc));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.tbSort.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
            }
        });

        TabLayout.Tab tab = binding.tbSort.getTabAt(0);
        tab.select();
    }

    private void setFragment(int pos){
        Fragment fragment = null;
        switch (pos) {

            case 1:
                fragment = new UpdateEmailMobileFragment();
                break;

            case 0:
                fragment = new UploadPanFragment();
                break;

            case 2:
                fragment = new BankDetailsFragment();
                break;
        }

        if (fragment != null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.simpleFrameLayout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

    @Override
    public void RetryResponse(String type) {

    }
}
