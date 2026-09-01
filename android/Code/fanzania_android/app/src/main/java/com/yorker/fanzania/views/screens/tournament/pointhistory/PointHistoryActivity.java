package com.yorker.fanzania.views.screens.tournament.pointhistory;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ActivityPointHistoryBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.views.screens.tournament.pointhistory.adapter.MyPointListAdapter;
import com.yorker.fanzania.views.screens.tournament.pointsbreakdown.PointsBreakDownActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import java.util.LinkedList;

public class PointHistoryActivity extends BaseActivity<PointHistoryPresenter>
        implements PointHistoryPresenter.IMainView, MyPointListAdapter.IPoints {


    private PointHistoryPresenter presenter;
    private ActivityPointHistoryBinding binding;


    @Override
    protected PointHistoryPresenter onCreatePresenter() {
        presenter = new PointHistoryPresenter(this, PointHistoryActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, PointHistoryPresenter presenter) {
        PointHistoryPresenterComponent component1 = DaggerPointHistoryPresenterComponent.builder()
                .presenterComponent(component)
                .pointHistoryApplicationModule(new PointHistoryApplicationModule(PointHistoryActivity.this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_point_history);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_mypointhistory));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.inRVList.rvList.setHasFixedSize(true);
        LinkedList<String> list = new LinkedList<>();
        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(PointHistoryActivity.this));
        MyPointListAdapter pAdapter = new MyPointListAdapter(PointHistoryActivity.this, list, this);
        binding.inRVList.rvList.setAdapter(pAdapter);
    }

    @Override
    public void OnClick() {
        startActivity(new Intent(this, PointsBreakDownActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void RetryResponse(String type) {

    }
}
