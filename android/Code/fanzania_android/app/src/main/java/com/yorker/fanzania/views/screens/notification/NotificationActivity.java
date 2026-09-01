package com.yorker.fanzania.views.screens.notification;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityNotificationBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseFragment<NotificationPresenter>
        implements NotificationPresenter.IMainView, NotificationListAdapter.INotification {

    private NotificationPresenter presenter;
    private ActivityNotificationBinding binding;
    private List<NotificationResponse> list;
    private MyFragmentCallback callback;

    @Override
    protected NotificationPresenter onCreatePresenter() {
        presenter = new NotificationPresenter(this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, NotificationPresenter presenter) {
        NotificationPresenterComponent splashPresenterComponent = DaggerNotificationPresenterComponent.builder()
                .presenterComponent(component)
                .notificationApplicationModule(new NotificationApplicationModule(getActivity()))
                .build();
        splashPresenterComponent.inject(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.activity_notification, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        binding.pullToRefresh.setOnRefreshListener(() -> {
            getList();
            binding.rvMatches.setVisibility(View.GONE);
            binding.tvNoMatch.setVisibility(View.GONE);
            binding.pBar.setVisibility(View.VISIBLE);
            binding.pullToRefresh.setRefreshing(false);
        });

        binding.btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternetConnection()){
                    presenter.deleteNotification(-1);
                }
                else
                    new NoNetworkDialog(getContext(),NotificationActivity.this, Constants.APICALL_2);
            }
        });
        getList();
    }

    private void getList(){
        if (CheckInternetConnection()){
            presenter.getNotifications();
            binding.rvMatches.setVisibility(View.GONE);
            binding.tvNoMatch.setVisibility(View.GONE);
            binding.pBar.setVisibility(View.VISIBLE);
        }
        else
            new NoNetworkDialog(getContext(),this, Constants.APICALL_1);
    }

    @Override
    public void RetryResponse(String type) {
        presenter.getNotifications();
    }

    @Override
    public void getNotificationsList(JSONObject jsonObject) {
        System.out.println("notification response "+jsonObject.toString());
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                list = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<NotificationResponse>>() {
                                }.getType())
                );

                if (list.size() > 0) {
                    binding.rvMatches.setVisibility(View.VISIBLE);
                    binding.tvNoMatch.setVisibility(View.GONE);
                    binding.pBar.setVisibility(View.GONE);
                    binding.btnClearAll.setVisibility(View.VISIBLE);
                    initList();
                } else {
                    binding.rvMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.VISIBLE);
                    binding.pBar.setVisibility(View.GONE);
                    binding.tvNoMatch.setText(jsonObject.getString("statusMessage"));
                    binding.btnClearAll.setVisibility(View.GONE);
                }
            } else{
                binding.rvMatches.setVisibility(View.GONE);
                binding.tvNoMatch.setVisibility(View.VISIBLE);
                binding.pBar.setVisibility(View.GONE);
                binding.btnClearAll.setVisibility(View.GONE);
                binding.tvNoMatch.setText(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initList() {
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMatches.addItemDecoration(new ItemDecoration(getContext()));
        NotificationListAdapter lAdapter = new NotificationListAdapter( list,this);
        binding.rvMatches.setAdapter(lAdapter);
    }

    @Override
    public void deleteNotification(NotificationResponse val) {
        presenter.deleteNotification(val.getNotificationId());
    }

    @Override
    public void onSuccessDeleteNotification(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
//                getList();
                callback.refreshCount();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            CustomToast.getInstance(getContext()).showSmallCustomToast(getString(R.string.error_somethingwentwrong));
            getList();
        }
    }

    public interface MyFragmentCallback{
        void refreshCount();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (MyFragmentCallback) context;
    }
}
