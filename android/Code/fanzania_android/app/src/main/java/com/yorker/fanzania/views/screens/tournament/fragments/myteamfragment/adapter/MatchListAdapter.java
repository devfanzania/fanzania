package com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildTournamentTeamItemNewBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.model.Matches;

import java.util.LinkedList;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {

    private Context context;
    private IMatch callback = null;
    private Boolean isActiveTournament;
    private Boolean isUpComingMatchSelected = false;
    private int prevPos = -1;
    private LinkedList<Matches> lList;

    public MatchListAdapter(Context context, LinkedList<Matches> list, Boolean isActiveTournament, IMatch iMatch) {
        this.context = context;
        this.lList = list;
        this.isActiveTournament = isActiveTournament;
        this.callback = iMatch;
    }

    @NonNull
    @Override
    public MatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildTournamentTeamItemNewBinding binding
                = ChildTournamentTeamItemNewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchListAdapter.ViewHolder holder, int position) {

        String text=lList.get(position).getTeam1ShortName()+" v "+lList.get(position).getTeam2ShortName();
        String textNo=lList.get(position).getMatchNo();
        holder.binding.tvTeamName.setText(text);
        holder.binding.teamSerial.setText("#"+textNo);

//        if (lList.get(position).getBattingTeam().equalsIgnoreCase(lList.get(position).getTeam1())){
//            holder.binding.imgBattingFirst.setVisibility(View.VISIBLE);
//            holder.binding.imgBattingSecond.setVisibility(View.GONE);
//        }else if(lList.get(position).getBattingTeam().equalsIgnoreCase(lList.get(position).getTeam2())){
//            holder.binding.imgBattingSecond.setVisibility(View.VISIBLE);
//            holder.binding.imgBattingFirst.setVisibility(View.GONE);
//        }
        switch (lList.get(position).getWeather()){
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
                holder.binding.tvStatus.setText(lList.get(position).getMatchDate());
                break;

            case "Live":
                holder.binding.imgTick.setVisibility(View.VISIBLE);
                holder.binding.imgTick.setImageResource(R.drawable.ic_round);
                holder.binding.tvStatus.setText(context.getString(R.string.text_live));
                break;

            case "UPCOMING":
                holder.binding.imgTick.setVisibility(View.GONE);
                holder.binding.tvStatus.setText(lList.get(position).getMatchDate());
                break;

            default:
                holder.binding.imgTick.setVisibility(View.GONE);
                holder.binding.tvStatus.setText(lList.get(position).getMatchDate());
                break;
        }

        if (lList.get(position).getBattingTeam().equalsIgnoreCase(lList.get(position).getTeam1()) && !lList.get(position).getMatchStatus().equalsIgnoreCase("complete")){
            holder.binding.tvStatus.setText(lList.get(position).getTeam1ShortName()+" to bat");
            holder.binding.imgTick.setVisibility(View.VISIBLE);
            holder.binding.imgTick.setImageResource(R.drawable.ic_round);
        }else if(lList.get(position).getBattingTeam().equalsIgnoreCase(lList.get(position).getTeam2()) && !lList.get(position).getMatchStatus().equalsIgnoreCase("complete")){
            holder.binding.tvStatus.setText(lList.get(position).getTeam2ShortName()+" to bat");
            holder.binding.imgTick.setVisibility(View.VISIBLE);
            holder.binding.imgTick.setImageResource(R.drawable.ic_round);
        }

        if (lList.get(position).getSelected())
            holder.binding.rlMain.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_red, context.getTheme()));
        else {
            holder.binding.rlMain.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_black, context.getTheme()));
        }
        if (isActiveTournament) {
            switch (lList.get(position).getMatchStatus()) {
                case "Live":
                    if (!isUpComingMatchSelected) {
                        isUpComingMatchSelected = true;
                        if (prevPos == -1) {
                            prevPos = position;
                            lList.get(position).setSelected(true);
                        } else {
                            lList.get(position).setSelected(true);
                            lList.get(prevPos).setSelected(false);
                        }
//                        callback.onSetMatch(position);
//                        recyclerView.scrollToPosition(position);
                        callback.onClickMatchList(lList.get(position));
                        holder.binding.rlMain.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_red, context.getTheme()));
                    }
                    break;

                case "UPCOMING":
                    if (!isUpComingMatchSelected) {
                        isUpComingMatchSelected = true;
                        if (prevPos == -1) {
                            prevPos = position;
                            lList.get(position).setSelected(true);
                        } else {
                            lList.get(position).setSelected(true);
                            lList.get(prevPos).setSelected(false);
                            prevPos = position;
                        }
//                        callback.onSetMatch(position);
//                        recyclerView.scrollToPosition(position);
                        callback.onClickMatchList(lList.get(position));
                        holder.binding.rlMain.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_red, context.getTheme()));
                    }
                    break;
            }
        }else {
            if (!isUpComingMatchSelected) {
                isUpComingMatchSelected = true;
                if (prevPos == -1) {
                    prevPos = 0;
                    lList.get(0).setSelected(true);
                }
                callback.onClickMatchList(lList.get(position));
                holder.binding.rlMain.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_red, context.getTheme()));
            }
        }

        holder.binding.rlMain.setOnClickListener(view -> {
            if (prevPos!=-1){
                if (prevPos!=position){
                    lList.get(position).setSelected(true);
                    lList.get(prevPos).setSelected(false);
                    prevPos = position;
                    callback.onClickMatchList(lList.get(position));
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void AddData(LinkedList<Matches> list, Boolean isActiveTournament) {
        this.isActiveTournament = isActiveTournament;
        isUpComingMatchSelected=false;
        prevPos=-1;
        lList = new LinkedList<>();
        lList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildTournamentTeamItemNewBinding binding;

        public ViewHolder(@NonNull ChildTournamentTeamItemNewBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IMatch {
        void onClickMatchList(Matches matches);
//        void onSetMatch(int position);
    }
}
