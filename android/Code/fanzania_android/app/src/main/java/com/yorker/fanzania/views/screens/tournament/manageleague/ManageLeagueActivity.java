package com.yorker.fanzania.views.screens.tournament.manageleague;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.yorker.fanzania.R;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityManageLeagueBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.CommonDialog;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

public class ManageLeagueActivity extends BaseActivity<ManageLeaguePresenter>
        implements ManageLeaguePresenter.IMainView,CommonDialog.ICommonDialog {

    private ManageLeaguePresenter presenter;
    private ActivityManageLeagueBinding binding;

    @Override
    protected ManageLeaguePresenter onCreatePresenter() {
        presenter=new ManageLeaguePresenter(this,this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, ManageLeaguePresenter presenter) {
        ManageLeaguePresenterComponent component1= DaggerManageLeaguePresenterComponent.builder()
                .presenterComponent(component)
                .manageLeagueApplicationModule(new ManageLeagueApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_manage_league);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_manageleague));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null){
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
//
//        MenuItem item_top1 = menu.findItem(R.id.action_item_two);
//        item_top1.setIcon(getResources().getDrawable(R.drawable.ic_delete, Objects.requireNonNull(this).getTheme()));
//
//        MenuItem item_top = menu.findItem(R.id.action_item_one);
//        item_top.setVisible(false);
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId())
//        {
//            case R.id.action_item_two:
//                String txt=getString(R.string.text_deleteleaguetxt)+" Demo league ?";
//                new CommonDialog(this,getString(R.string.text_deleteleageu),txt,this);
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void PositiveResponse(Boolean value) {
        CustomToast.getInstance(this).showSmallCustomToast("League deleted successfully");
        finish();
    }

    @Override
    public void RetryResponse(String type) {

    }
}
