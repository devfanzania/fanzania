package com.yorker.fanzania.views.screens.matchcontest.scorecard;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityScoreCardBinding;
import com.yorker.fanzania.databinding.ItemBowlerBinding;
import com.yorker.fanzania.databinding.ItemDeviderBinding;
import com.yorker.fanzania.databinding.ItemDidnotbatBinding;
import com.yorker.fanzania.databinding.ItemScorecardBinding;
import com.yorker.fanzania.databinding.ItemScorecardBowlerBinding;
import com.yorker.fanzania.databinding.ItemScoreextrasBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ScoreCardActivity extends BaseActivity<ScoreCardPresenter>
        implements ScoreCardPresenter.IMainView {

    private ActivityScoreCardBinding binding;
    private ScoreCardPresenter presenter;
    private List<ScoreCardResponse> inning1List;
    private List<ScoreCardResponse> inning2List;
    private int matchID=0;

    @Override
    protected ScoreCardPresenter onCreatePresenter() {
        presenter = new ScoreCardPresenter(this, ScoreCardActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, ScoreCardPresenter presenter) {
        ScoreCardComponent component1 = DaggerScoreCardComponent.builder()
                .presenterComponent(component)
                .scoreCardApplicationModule(new ScoreCardApplicationModule(ScoreCardActivity.this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_score_card);

        if (getIntent()!=null){
            matchID=getIntent().getIntExtra(Constants.TAG_MATCHID,0);
        }

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getScore();
    }

    private void getScore() {
        if (CheckInternetConnection())
            presenter.getScoreCards(matchID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initView() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getIntent().getStringExtra(Constants.TAG_HEADER));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());


        binding.pullToRefresh.setOnRefreshListener(() -> {
            getScore();
            binding.pullToRefresh.setRefreshing(false);
        });
    }

    @Override
    public void RetryResponse(String type) {
        presenter.getScoreCards(57);
    }

    @Override
    public void getScores(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                List<ScoreCardResponse> sList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<ScoreCardResponse>>() {
                                }.getType())
                );

                if (sList.size() > 0) {
                    inning1List = new ArrayList<>();
                    inning2List = new ArrayList<>();

                    int inning = sList.get(0).getInning();

                    for (ScoreCardResponse data : sList) {
                        if (inning == data.getInning())
                            inning1List.add(data);
                        else
                            inning2List.add(data);
                    }

                    binding.pBar.setVisibility(View.GONE);
                    setHeaderData(sList.get(0));
                }else {
                    binding.pBar.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                }
            } else{
                binding.pBar.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHeaderData(ScoreCardResponse obj) {

        binding.tvBottomText.setText(obj.getMatchSummary());
//        MS ~141/6 ~ov 20.0
        // ------------ Left is always team 1 -------//
        if (obj.getTeam1Score() != null && obj.getTeam1Score().length() > 0) {
            String[] arr = obj.getTeam1Score().split("~");
            binding.tvScoreLeft.setText(arr[1]);
//            binding.tvOver.setText(new StringBuilder().append(arr[2]).append(" ").append(arr[3]));
            binding.tvOver.setText(arr[2]);

        } else {
            binding.tvScoreLeft.setText(getString(R.string.text_tobat));
            binding.tvOver.setText("-");
        }

        // ------------ Right is always team 2 -------//
        if (obj.getTeam2Score() != null && obj.getTeam2Score().length() > 0) {
            String[] arr = obj.getTeam2Score().split("~");
            binding.tvScoreRight.setText(arr[1]);
//            binding.tvRightOver.setText(new StringBuilder().append(arr[2]).append(" ").append(arr[3]));
            binding.tvRightOver.setText(arr[2]);
        } else {
            binding.tvScoreRight.setText(getString(R.string.text_tobat));
            binding.tvRightOver.setText("-");
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.blank_jersey);
        requestOptions.error(R.drawable.blank_jersey);

        String url = Constants.BASE_IMAGE_URL + obj.getTeam1Image();
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(url).into(binding.imgLeft);

        String url1 = Constants.BASE_IMAGE_URL + obj.getTeam2Image();
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(url1).into(binding.imgRight);

        binding.rlHeader.setVisibility(View.VISIBLE);
        binding.rlBody.setVisibility(View.VISIBLE);
        setInning1Data();
        setInning2Data();
    }

    //--------------- Innings 1 data -------------//
    private void setInning1Data() {
        binding.tvInnig1Name.setText(new StringBuilder().append(inning1List.get(0).getTeamSortName())
                .append(" ")
                .append(getString(R.string.text_inning)));

        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        boolean isBatsmanLayoutAdded = false;
        boolean isBowlerLayoutAdded = false;

        binding.llInning1.removeAllViews();
        StringBuilder strNotBat = new StringBuilder();

        //------------- Batsman ---------//
        for (ScoreCardResponse data : inning1List) {
            if (data.getInningDesc().equals("Batting")) {
                if (!isBatsmanLayoutAdded) {
                    View to_add = linflater.inflate(R.layout.item_scorecard_batsman,
                            binding.llInning1, false);
                    binding.llInning1.addView(to_add);
                    isBatsmanLayoutAdded = true;
                }

                if (data.getbTdismissalinfo() != null) {
                    ItemScorecardBinding item
                            = ItemScorecardBinding.inflate(linflater, binding.llInning1, false);

                    StringBuilder str=new StringBuilder();
                    str.append(data.getPlayerName());

                    if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("c"))
                        str.append("(c)");
                    else if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("vc"))
                        str.append("(vc)");

                    item.tvBatsman.setText(str);

                    item.tvStatus.setText(data.getbTdismissalinfo());
                    item.tvR.setText(String.valueOf(data.getbTrunScored()));
                    item.tvB.setText(String.valueOf(data.getbTballfaced()));
                    item.tv4S.setText(String.valueOf(data.getbTrun4s()));
                    item.tv6S.setText(String.valueOf(data.getbTrun6s()));
                    item.tvSR.setText(String.valueOf(Math.round(data.getbTstrikerate())));
                    binding.llInning1.addView(item.getRoot());
                } else {
                    if (strNotBat.length() == 0)
                        strNotBat.append(data.getPlayerName());
                    else
                        strNotBat.append(", ").append(data.getPlayerName());
                }
            }
        }

        // --------- Set Extras ----------//
        if (inning1List.get(0).getTeam1Extras() != null) {
            ItemScoreextrasBinding item
                    = ItemScoreextrasBinding.inflate(linflater, binding.llInning1, false);
            item.tvBatsman.setText(getString(R.string.text_extras));
            item.tvRun.setText(inning1List.get(0).getTeam1Extras());
            binding.llInning1.addView(item.getRoot());
        }

        //----------- Set Total -------//
        if (inning1List.get(0).getTeam1Score() != null && inning1List.get(0).getTeam1Score().length() > 0) {
            String[] arr = inning1List.get(0).getTeam1Score().split("~");
            ItemScoreextrasBinding item
                    = ItemScoreextrasBinding.inflate(linflater, binding.llInning1, false);
            item.tvBatsman.setText(getString(R.string.text_total));
            item.tvRun.setText(new StringBuilder().append(arr[1]).append(" ").append(arr[2]));
            item.tvRR.setText(new StringBuilder().append("RR").append(" ").append(inning1List.get(0).getTeam1RR()));
            binding.llInning1.addView(item.getRoot());
        }

        //----------- Remaining Batsman -------//
        if (strNotBat.length() > 0) {
            ItemDidnotbatBinding item
                    = ItemDidnotbatBinding.inflate(linflater, binding.llInning1, false);
            item.tvRR.setText(strNotBat);
            binding.llInning1.addView(item.getRoot());
        }

        binding.llInning1.addView(ItemDeviderBinding.inflate(linflater, binding.llInning1, false).getRoot());

        //------------- Bowler ---------//
        for (ScoreCardResponse data : inning1List) {
            if (data.getInningDesc().equals("Bowling")) {
                if (!isBowlerLayoutAdded) {
                    binding.llInning1.addView(ItemScorecardBowlerBinding.inflate(linflater, binding.llInning1, false).getRoot());
                    isBowlerLayoutAdded = true;
                }
                ItemBowlerBinding item
                        = ItemBowlerBinding.inflate(linflater, binding.llInning1, false);

                StringBuilder str=new StringBuilder();
                str.append(data.getPlayerName());

                if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("c"))
                    str.append("(c)");
                else if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("vc"))
                    str.append("(vc)");

                item.tvBatsman.setText(str);

                item.tvR.setText(String.valueOf(data.getbLover()));
                item.tvB.setText(String.valueOf(data.getbLmaiden()));
                item.tv4S.setText(String.valueOf(data.getbLrun()));
                item.tv6S.setText(String.valueOf(data.getbLwicket()));
                item.tvSR.setText(String.valueOf(data.getbLecon()));
                binding.llInning1.addView(item.getRoot());
            }
        }
    }

    //--------------- Innings 2 data -------------//
    private void setInning2Data() {
        binding.tvInnig2Name.setText(new StringBuilder().append(inning2List.get(0).getTeamSortName())
                .append(" ")
                .append(getString(R.string.text_inning)));

        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        boolean isBatsmanLayoutAdded = false;
        boolean isBowlerLayoutAdded = false;

        binding.llInning2.removeAllViews();
        StringBuilder strNotBat = new StringBuilder();

        //------------- Batsman ---------//
        for (ScoreCardResponse data : inning2List) {
            if (data.getInningDesc().equals("Batting")) {
                if (!isBatsmanLayoutAdded) {
                    View to_add = linflater.inflate(R.layout.item_scorecard_batsman,
                            binding.llInning2, false);
                    binding.llInning2.addView(to_add);
                    isBatsmanLayoutAdded = true;
                }

                if (data.getbTdismissalinfo() != null) {
                    ItemScorecardBinding item
                            = ItemScorecardBinding.inflate(linflater, binding.llInning2, false);

                    StringBuilder str=new StringBuilder();
                    str.append(data.getPlayerName());

                    if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("c"))
                        str.append("(c)");
                    else if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("vc"))
                        str.append("(vc)");

                    item.tvBatsman.setText(str);

                    item.tvStatus.setText(data.getbTdismissalinfo());
                    item.tvR.setText(String.valueOf(data.getbTrunScored()));
                    item.tvB.setText(String.valueOf(data.getbTballfaced()));
                    item.tv4S.setText(String.valueOf(data.getbTrun4s()));
                    item.tv6S.setText(String.valueOf(data.getbTrun6s()));
                    item.tvSR.setText(String.valueOf(Math.round(data.getbTstrikerate())));
                    binding.llInning2.addView(item.getRoot());
                } else {
                    if (strNotBat.length() == 0)
                        strNotBat.append(data.getPlayerName());
                    else
                        strNotBat.append(", ").append(data.getPlayerName());
                }
            }
        }

        // --------- Set Extras ----------//
        if (inning2List.get(0).getTeam2Extras() != null) {
            ItemScoreextrasBinding item
                    = ItemScoreextrasBinding.inflate(linflater, binding.llInning2, false);
            item.tvBatsman.setText(getString(R.string.text_extras));
            item.tvRun.setText(inning2List.get(0).getTeam2Extras());
            binding.llInning2.addView(item.getRoot());
        }

        //----------- Set Total -------//
        if (inning2List.get(0).getTeam2Score() != null && inning2List.get(0).getTeam2Score().length() > 0) {
            String[] arr = inning2List.get(0).getTeam2Score().split("~");
            ItemScoreextrasBinding item
                    = ItemScoreextrasBinding.inflate(linflater, binding.llInning2, false);
            item.tvBatsman.setText(getString(R.string.text_total));
            item.tvRun.setText(new StringBuilder().append(arr[1]).append(" ").append(arr[2]));
            item.tvRR.setText(new StringBuilder().append("RR").append(" ").append(inning2List.get(0).getTeam2RR()));
            binding.llInning2.addView(item.getRoot());
        }

        //----------- Remaining Batsman -------//
        if (strNotBat.length() > 0) {
            ItemDidnotbatBinding item
                    = ItemDidnotbatBinding.inflate(linflater, binding.llInning2, false);
            item.tvRR.setText(strNotBat);
            binding.llInning2.addView(item.getRoot());
        }

        binding.llInning2.addView(ItemDeviderBinding.inflate(linflater, binding.llInning2, false).getRoot());

        //------------- Bowler ---------//
        for (ScoreCardResponse data : inning2List) {
            if (data.getInningDesc().equals("Bowling")) {
                if (!isBowlerLayoutAdded) {
                    binding.llInning2.addView(ItemScorecardBowlerBinding.inflate(linflater, binding.llInning2, false).getRoot());
                    isBowlerLayoutAdded = true;
                }
                ItemBowlerBinding item
                        = ItemBowlerBinding.inflate(linflater, binding.llInning2, false);

                StringBuilder str=new StringBuilder();
                str.append(data.getPlayerName());

                if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("c"))
                    str.append("(c)");
                else if (data.getPlayerIndicator()!=null && data.getPlayerIndicator().equals("vc"))
                    str.append("(vc)");

                item.tvBatsman.setText(str);

                item.tvR.setText(String.valueOf(data.getbLover()));
                item.tvB.setText(String.valueOf(data.getbLmaiden()));
                item.tv4S.setText(String.valueOf(data.getbLrun()));
                item.tv6S.setText(String.valueOf(data.getbLwicket()));
                item.tvSR.setText(String.valueOf(data.getbLecon()));
                binding.llInning2.addView(item.getRoot());
            }
        }
    }
}
