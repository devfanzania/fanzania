package com.yorker.fanzania.views.screens.matchcontest.fragments.leagues;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildMatchcontestTeamItemBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.teams.McTeam;
import com.yorker.fanzania.widgets.DateUtils;

import java.util.List;

public class McMatchListAdapter extends RecyclerView.Adapter<McMatchListAdapter.ViewHolder> {

    private IMatch callback = null;
    private int prevPos = -1;
    private List<DailyMatchModel> lList;
//    private Boolean isMatchSelected = false;
    private int cMatchID;
    String battingTeam = "";

    public void setBattingTeam(String battingTeam){
        this.battingTeam = battingTeam;
    }
    public McMatchListAdapter(List<DailyMatchModel> list, IMatch iMatch, int matchID) {
        this.lList = list;
        this.callback = iMatch;
        this.cMatchID = matchID;
    }

    @NonNull
    @Override
    public McMatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildMatchcontestTeamItemBinding binding
                = ChildMatchcontestTeamItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull McMatchListAdapter.ViewHolder holder, int position) {

        String textNo= String.valueOf(lList.get(position).getMatchNo());
//        holder.binding.tvTeamName.setText((new StringBuilder().append(textNo).append(" ")).append(lList.get(position).getTeam1ShortName())
//                .append(" v ")
//                .append(lList.get(position).getTeam2ShortName()));
        holder.binding.tvTeamName.setText((new StringBuilder()).append(lList.get(position).getTeam1ShortName())
                .append(" v ")
                .append(lList.get(position).getTeam2ShortName()));
        String weather = lList.get(position).getWeather() == null ? "null" : lList.get(position).getWeather() ;

        Log.e("battingTeam_",""+this.battingTeam+" Team1- "+lList.get(position).getTeam1()+" Team2- "+lList.get(position).getTeam2());
//        if (this.battingTeam.equalsIgnoreCase(lList.get(position).getTeam1())){
//            holder.binding.imgBattingFirst.setVisibility(View.VISIBLE);
//            holder.binding.imgBattingSecond.setVisibility(View.GONE);
//        }else if(this.battingTeam.equalsIgnoreCase(lList.get(position).getTeam2())){
//            holder.binding.imgBattingSecond.setVisibility(View.VISIBLE);
//            holder.binding.imgBattingFirst.setVisibility(View.GONE);
//        }

        switch (weather){
            case "sunny":
                holder.binding.imgWeather.setVisibility(View.VISIBLE);
                holder.binding.imgWeather.setImageResource(R.drawable.sunny);
                break;

            case "rain":
                holder.binding.imgWeather.setVisibility(View.VISIBLE);
                holder.binding.imgWeather.setImageResource(R.drawable.rain);
                break;

            case "snow":
                holder.binding.imgWeather.setVisibility(View.VISIBLE);
                holder.binding.imgWeather.setImageResource(R.drawable.snow);
                break;

            case "cloudy":
                holder.binding.imgWeather.setVisibility(View.VISIBLE);
                holder.binding.imgWeather.setImageResource(R.drawable.cloudy);
                break;

            case "thunderstorms":
                holder.binding.imgWeather.setVisibility(View.VISIBLE);
                holder.binding.imgWeather.setImageResource(R.drawable.thunderstorms);
                break;

            default:
                holder.binding.imgWeather.setVisibility(View.INVISIBLE);
                break;
        }

        switch (lList.get(position).getMatchStatus()) {
            case "COMPLETE":
                holder.binding.imgTick.setVisibility(View.VISIBLE);
                holder.binding.imgTick.setImageResource(R.drawable.ic_check_black);
                DateUtils.getDateFromISO(lList.get(position).getMatchDate(), holder.binding.tvStatus);
                break;

            case "Live":
                holder.binding.imgTick.setVisibility(View.VISIBLE);
                holder.binding.imgTick.setImageResource(R.drawable.ic_round);
                DateUtils.getDateFromISO(lList.get(position).getMatchDate(), holder.binding.tvStatus);
                break;

            default:
                holder.binding.imgTick.setVisibility(View.GONE);
                DateUtils.getDateFromISO(lList.get(position).getMatchDate(), holder.binding.tvStatus);
                break;
        }

        if (this.battingTeam.equalsIgnoreCase(lList.get(position).getTeam1()) && !lList.get(position).getMatchStatus().equalsIgnoreCase("complete")){
            holder.binding.tvStatus.setText(lList.get(position).getTeam1ShortName()+" to bat");
            holder.binding.imgTick.setVisibility(View.VISIBLE);
            holder.binding.imgTick.setImageResource(R.drawable.ic_round);
        }else if(this.battingTeam.equalsIgnoreCase(lList.get(position).getTeam2()) && !lList.get(position).getMatchStatus().equalsIgnoreCase("complete")){
            holder.binding.tvStatus.setText(lList.get(position).getTeam2ShortName()+" to bat");
            holder.binding.imgTick.setVisibility(View.VISIBLE);
            holder.binding.imgTick.setImageResource(R.drawable.ic_round);
        }

        if (lList.get(position).isSelected()) {
            prevPos=position;
            holder.binding.rlMain.setBackground(holder.binding.getRoot().getContext().getResources().getDrawable(R.drawable.tab_round_corner_red,
                    holder.binding.getRoot().getContext().getTheme()));
            callback.onClickMatchList(lList.get(position));
        } else {
            holder.binding.rlMain.setBackground(holder.binding.getRoot().getContext().getResources().getDrawable(R.drawable.tab_round_corner_dark,
                    holder.binding.getRoot().getContext().getTheme()));
        }

        if (cMatchID != 0 && cMatchID == lList.get(position).getMatchId()) {
//            isMatchSelected = true;
            cMatchID = 0;
            if (prevPos == -1) {
                lList.get(0).setSelected(false);
                prevPos = position;
                lList.get(position).setSelected(true);
            } else {
                lList.get(position).setSelected(true);
                lList.get(prevPos).setSelected(false);
                prevPos = position;
            }
            callback.onClickMatchList(lList.get(position));
            holder.binding.rlMain.setBackground(holder.binding.getRoot().getContext().getResources().getDrawable(R.drawable.tab_round_corner_red,
                    holder.binding.getRoot().getContext().getTheme()));
        }

        holder.binding.rlMain.setOnClickListener(view -> {
            if (prevPos > -1) {
                if (prevPos != position) {
                    lList.get(position).setSelected(true);
                    lList.get(prevPos).setSelected(false);
                    prevPos = position;
                }
            } else {
                lList.get(position).setSelected(true);
                prevPos = position;
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildMatchcontestTeamItemBinding binding;

        public ViewHolder(@NonNull ChildMatchcontestTeamItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IMatch {
        void onClickMatchList(DailyMatchModel matches);
    }
}
