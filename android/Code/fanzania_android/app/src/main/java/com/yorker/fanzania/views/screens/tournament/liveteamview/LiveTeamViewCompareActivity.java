package com.yorker.fanzania.views.screens.tournament.liveteamview;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.naqdi.chart.model.Line;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityLiveTeamViewBinding;
import com.yorker.fanzania.databinding.ActivityLiveTeamViewCompareBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.TeamCompareDialog;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.adapter.LiveMatchCompareAdapter;
import com.yorker.fanzania.views.model.LiveTeamScoreComparison;
import com.yorker.fanzania.views.model.TeamPointsComparison;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.LiveScoreFragment;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;
import com.yorker.fanzania.views.screens.tournament.liveteamview.adapter.LiveTeamPlayerListAdapter;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveTeamViewCompareActivity extends BaseActivity<LiveTeamViewPresenter> {

    private LiveTeamViewPresenter presenter;
    private ActivityLiveTeamViewCompareBinding binding;
    private String userID;
    private String myTeamId;
    private Handler handler = new Handler();

    List<TeamPointsComparison> teamPointsComparisonList = new ArrayList<>();
    List<LiveTeamScoreComparison> liveTeamScoreComparisonList = new ArrayList<>();

    private String tournamentID;
    private String customerId;
    private String userTeamId;
    private String matchID;
    private String userTeamName;
    private int tabPosition=0;

    @Override
    protected LiveTeamViewPresenter onCreatePresenter() {
        presenter=new LiveTeamViewPresenter(null,this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LiveTeamViewPresenter presenter) {
        LiveTeamViewPresenterComponent component1 = DaggerLiveTeamViewPresenterComponent.builder()
                .presenterComponent(component)
                .liveTeamViewApplicationModule(new LiveTeamViewApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_live_team_view_compare);
        binding.tvTeamName.setText(userTeamName);
        //getPlayersWithPoints(tournamentID, userTeamId, customerId, matchType);
        binding.imgBack.setOnClickListener(view -> onBackPressed());
        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent()!=null) {
            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            matchID = getIntent().getStringExtra(Constants.TAG_MATCHID);
            userTeamId = getIntent().getStringExtra(Constants.TAG_USERTEAMID);
            userID = getIntent().getStringExtra(Constants.TAG_ID);
            customerId = userID;
            myTeamId = getIntent().getStringExtra("myTeamId");
        }
        setupTabIcons();
        getLiveComparison(tournamentID, userTeamId, customerId, matchID);
        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.lLiveMatchCompare.setVisibility(View.VISIBLE);
                    binding.lTotalPointCompare.setVisibility(View.GONE);
                    getLiveComparison(tournamentID, userTeamId, customerId, matchID);
                } else {
                    binding.lTotalPointCompare.setVisibility(View.VISIBLE);
                    binding.lLiveMatchCompare.setVisibility(View.GONE);
                }
                tabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private LineData generateDataLine(List<TeamPointsComparison> teamPointsComparisonList, String myTeamName, String otherTeamName) {

        List<String> matches = new ArrayList<>();
        for (int i=0; i<teamPointsComparisonList.size();i++){
            matches.add(String.valueOf(i));
        }

        binding.chart1.getDescription().setEnabled(false);
        binding.chart1.setDrawGridBackground(false);
        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = binding.chart1.getXAxis();

            // vertical grid lines
            //xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setAxisMinimum(0f);
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setValueFormatter(new ValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    Log.e("value",matches.get((int) value % matches.size()));
//                    return "1";
//                }
//            });
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = binding.chart1.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            binding.chart1.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMinimum(0f);
            float myPoints = Float.parseFloat(teamPointsComparisonList.get(teamPointsComparisonList.size() -1).getMyMatchTotalPoints());
            float otherPoints = Float.parseFloat(teamPointsComparisonList.get(teamPointsComparisonList.size() -1).getOtherMatchTotalPoints());
            if (myPoints >= otherPoints){
                yAxis.setAxisMaximum(Math.round(myPoints));
            }else{
                yAxis.setAxisMaximum(Math.round(otherPoints));
            }
        }

//        {   // // Create Limit Lines // //
//            LimitLine llXAxis = new LimitLine(9f, "Index 10");
//            llXAxis.setLineWidth(4f);
//            llXAxis.enableDashedLine(10f, 10f, 0f);
//            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            llXAxis.setTextSize(10f);
//
//            // draw limit lines behind data instead of on top
//            yAxis.setDrawLimitLinesBehindData(true);
//            xAxis.setDrawLimitLinesBehindData(true);
//
//            // add limit lines
////            yAxis.addLimitLine(ll1);
////            yAxis.addLimitLine(ll2);
////            xAxis.addLimitLine(llXAxis);
//        }

        ArrayList<Entry> values1 = new ArrayList<>();

        values1.add(new Entry(0, 0));
//        values1.add(new Entry(1, 600));
//        values1.add(new Entry(2, 855));

        LineDataSet d1 = new LineDataSet(values1, myTeamName);
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(getResources().getColor(R.color.colorRed));
        d1.setColor(getResources().getColor(R.color.colorRed));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        values2.add(new Entry(0, 0));
//        values2.add(new Entry(1, 0));
//        values2.add(new Entry(2, 471));

        for(TeamPointsComparison teamPointsComparison : teamPointsComparisonList){
            values1.add(new Entry(Float.parseFloat(teamPointsComparison.getMatchNo()), Float.parseFloat(teamPointsComparison.getMyMatchTotalPoints())));
            values2.add(new Entry(Float.parseFloat(teamPointsComparison.getMatchNo()),Float.parseFloat(teamPointsComparison.getOtherMatchTotalPoints())));
        }
        LineDataSet d2 = new LineDataSet(values2, otherTeamName);
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(getResources().getColor(R.color.colorBlue));
        d2.setColor(getResources().getColor(R.color.colorBlue));
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        binding.chart1.setData(new LineData(sets));
        return new LineData(sets);
    }

    private void setDelay() {
        new Handler().postDelayed(() -> {
        }, 150);
    }

    private void setupTabIcons() {
        binding.tbTabs.addTab(binding.tbTabs.newTab().setText("Live match comparison"));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setText("Total points comparison"));
        binding.tbTabs.getTabAt(0).select();
    }

    public void getTeamPointsComparison(String tournamentID, String userTeamID, String userID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put("MyTeamId", myTeamId);
        if (myTeamId != null){
            map.put("OtherTeamId", userTeamID);
        }

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.TeamPointsComparison(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                String.valueOf(userID),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        List<TeamPointsComparison> playerList = new ArrayList<>(
                                new Gson().fromJson(
                                        jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                        , new TypeToken<List<TeamPointsComparison>>() {
                                        }.getType())
                        );
                        if (playerList.size() > 0) {
                            teamPointsComparisonList.clear();
                            teamPointsComparisonList.addAll(playerList);
                            binding.tvFirstTotal.setText(teamPointsComparisonList.get(teamPointsComparisonList.size() - 1).getMyMatchTotalPoints());
                            binding.tvSecondTotal.setText(teamPointsComparisonList.get(teamPointsComparisonList.size() - 1).getOtherMatchTotalPoints());
                            setPlayerData(liveTeamScoreComparisonList, teamPointsComparisonList);

                        } else {
                            binding.inRVList.rvList.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void getLiveComparison(String tournamentID, String userTeamID, String userID, String matchId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put("MyTeamId", myTeamId);
        map.put("MatchId", matchId);
        if (myTeamId != null){
            map.put("OtherTeamId", userTeamID);
        }

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.LiveMatchScoreComparison(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                String.valueOf(userID),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        List<LiveTeamScoreComparison> playerList = new ArrayList<>(
                                new Gson().fromJson(
                                        jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                        , new TypeToken<List<LiveTeamScoreComparison>>() {
                                        }.getType())
                        );
                        if (playerList.size() > 0) {
                            liveTeamScoreComparisonList.clear();
                            liveTeamScoreComparisonList.addAll(playerList);
                            getTeamPointsComparison(tournamentID, userTeamId, customerId);
                        } else {
                            binding.inRVList.rvList.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    @SuppressLint("CheckResult")
    private void setPlayerData(List<LiveTeamScoreComparison> playerData, List<TeamPointsComparison> teamPointsComparisonList) {

        LiveMatchCompareAdapter pAdapter = new LiveMatchCompareAdapter( playerData);
        binding.inRVList.rvList.setAdapter(pAdapter);
        binding.inRVList.rvList.setVisibility(View.VISIBLE);
        binding.inRVList.pBar.setVisibility(View.GONE);

        binding.tvMyTeamName.setText(playerData.get(0).getMyTeamName());
        binding.tvOtherTeamName.setText(playerData.get(0).getOtherTeamName());
        binding.tvTeamName.setText(playerData.get(0).getOtherTeamName());

        if (playerData.get(0).getMyPrediction() == null){
            binding.predFirst.setText("-");
        }else{
            binding.predFirst.setText(playerData.get(0).getMyPrediction());
        }
        if (playerData.get(0).getOtherPrediction() == null){
            binding.predSecond.setText("-");
        }else{
            binding.predSecond.setText(""+playerData.get(0).getOtherPrediction());
        }
        setThisMatch(playerData, teamPointsComparisonList);
    }

    private void setGraphData(float myMatchPoints,float a, float b, String team, String teamB){
        List<String> intervalList = listOf("M0", "M1", "M2");
        List<String>  rangeList = listOf("0", "100", "200", "300", "400", "500","600","700","800","900");
        Line line = new Line(team, Color.BLUE, listOf(myMatchPoints, a));
        Line line1 = new Line(teamB, Color.RED, listOf(0f, b));
        List<Line> lineList = new ArrayList<>();
        lineList.add(line);
        lineList.add(line1);
        binding.chainChartView.setData(lineList, intervalList, rangeList);
    }

    private void setThisMatch(List<LiveTeamScoreComparison> playerData, List<TeamPointsComparison> teamPointsComparisonList) {
        int a = 0, b = 0;
        for (LiveTeamScoreComparison liveTeamScoreComparison: playerData){
            a = a + liveTeamScoreComparison.getMyTotalPoints();
            b = b + liveTeamScoreComparison.getOtherTotalPoints();
        }

        for (TeamPointsComparison teamPointsComparison : teamPointsComparisonList){
            if (!teamPointsComparison.getMatchStatus().equalsIgnoreCase("complete")){
                binding.liveTeamName.setText("M"+teamPointsComparison.getMatchNo()+" | "+ LiveScoreFragment.TEAM1NAME+ " V "+LiveScoreFragment.TEAM2NAME);
            }
        }
        binding.tvFirstThisMatch.setText(""+a);
        binding.tvSecondThisMatch.setText(""+b);

        binding.tvThisMatch.setText("THIS MATCH");
        binding.tvThisMatch1.setText("THIS MATCH");

        binding.totalHeading.setText("TOTAL");
        binding.totalHading1.setText("TOTAL");

//        binding.tvFirstTotal.setText(""+a);
//        binding.tvSecondTotal.setText(""+b);

        setGraphData(Float.parseFloat(teamPointsComparisonList.get(0).getMyMatchTotalPoints()),Float.parseFloat(teamPointsComparisonList.get(0).getMyMatchTotalPoints()), Float.parseFloat(teamPointsComparisonList.get(0).getOtherMatchTotalPoints()), playerData.get(0).getMyTeamName(),playerData.get(0).getOtherTeamName());
        generateDataLine(teamPointsComparisonList, playerData.get(0).getMyTeamName(), playerData.get(0).getOtherTeamName());
        binding.tvTeamPoints.setText(teamPointsComparisonList.get(teamPointsComparisonList.size() - 1).getMyMatchTotalPoints()+"\n"+playerData.get(0).getMyTeamName());
        binding.tvTeamPointsOther.setText(teamPointsComparisonList.get(teamPointsComparisonList.size() - 1).getOtherMatchTotalPoints()+"\n"+playerData.get(0).getOtherTeamName());

        int pointDiff = Math.round(Float.parseFloat(teamPointsComparisonList.get(teamPointsComparisonList.size() - 1).getMyMatchTotalPoints())-Float.parseFloat(teamPointsComparisonList.get(teamPointsComparisonList.size() - 1).getOtherMatchTotalPoints()));
        int pointDiff1 = a - b;//first page
        if (pointDiff1 > 0 ){
            binding.tvPointDif.setText("+"+pointDiff1);
            binding.tvPointDif.setTextColor(Color.parseColor("#497804"));
            binding.tvTeamPerformance.setText("You are AHEAD "+playerData.get(0).getOtherTeamName()+" BY "+pointDiff1+" POINTS in this match");
        }else if(pointDiff1 < 0){
            binding.tvPointDif.setText(""+pointDiff1);
            binding.tvPointDif.setTextColor(Color.parseColor("#E95354"));
            String  di = String.valueOf(pointDiff1).replace("-","");
            binding.tvTeamPerformance.setText(Html.fromHtml("<html><body>You are BEHIND "+playerData.get(0).getOtherTeamName()+" BY <font color=red>"+di+"</font> POINTS in this match</body></html>"));
        }else if(pointDiff1 == 0){
            binding.tvPointDif.setText(""+pointDiff1);
            binding.tvPointDif.setTextColor(Color.parseColor("#E95354"));
            String  di = String.valueOf(pointDiff1).replace("-","");
            binding.tvTeamPerformance.setText(Html.fromHtml("<html><body>Both Teams Are on SAME Points <font color=red>"+di+"</font></body></html>"));
        }

        if (pointDiff > 0 ){
            binding.tvTeamPerformance1.setText("You are AHEAD by " +pointDiff+" points");
        }else if(pointDiff < 0){
            String aa = String.valueOf(pointDiff).replace("-","");
            binding.tvTeamPerformance1.setText(Html.fromHtml("<html><body>You are BEHIND By <font color=red>"+aa+"</font> points</body></html>"));
        }else if (pointDiff == 0){
            binding.tvTeamPerformance1.setText("Both Teams Are on SAME Points " +pointDiff);
        }
    }

    @Override
    public void RetryResponse(String type) {

    }
}
