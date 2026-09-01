package com.yorker.fanzania.views.screens.tournament.pointsbreakdown;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ActivityPointsBreakDownBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.views.screens.tournament.pointsbreakdown.adapter.MyPointBreakDownListAdapter;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import java.util.LinkedList;

public class PointsBreakDownActivity extends BaseActivity<PointBreakDownPresenter>
        implements PointBreakDownPresenter.IMainView, MyPointBreakDownListAdapter.IPoints {

    private PointBreakDownPresenter presenter;
    private ActivityPointsBreakDownBinding binding;

    @Override
    protected PointBreakDownPresenter onCreatePresenter() {
        presenter = new PointBreakDownPresenter(this, PointsBreakDownActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, PointBreakDownPresenter presenter) {
        PointBreakDownPresenterComponent component1 = DaggerPointBreakDownPresenterComponent.builder()
                .presenterComponent(component)
                .pointBreakDownApplicationModule(new PointBreakDownApplicationModule(PointsBreakDownActivity.this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_points_break_down);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_mypointbreak));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.inRVList.rvList.setHasFixedSize(true);
        LinkedList<String> list = new LinkedList<>();
        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(PointsBreakDownActivity.this));
        MyPointBreakDownListAdapter pAdapter = new MyPointBreakDownListAdapter(PointsBreakDownActivity.this, list, this);
        binding.inRVList.rvList.setAdapter(pAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void RetryResponse(String type) {

    }
}
