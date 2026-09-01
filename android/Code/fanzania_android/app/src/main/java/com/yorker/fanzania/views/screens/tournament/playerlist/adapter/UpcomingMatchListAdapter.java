package com.yorker.fanzania.views.screens.tournament.playerlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildUpcomingMatchBinding;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.UpcomingMatchModel;

import java.util.LinkedList;

public class UpcomingMatchListAdapter extends RecyclerView.Adapter<UpcomingMatchListAdapter.ViewHolder> {

    private LinkedList<UpcomingMatchModel> lList;

    public UpcomingMatchListAdapter(LinkedList<UpcomingMatchModel> list) {
        this.lList = list;
    }

    @NonNull
    @Override
    public UpcomingMatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildUpcomingMatchBinding binding
                = ChildUpcomingMatchBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMatchListAdapter.ViewHolder holder, int position) {

        String text = lList.get(position).getTeam1ShortName() + " v " + lList.get(position).getTeam2ShortName();
//        holder.binding.tvTeamName.setText(text);
        String textNo= String.valueOf(lList.get(position).getMatchNo());
        holder.binding.tvTeamName.setText(text);
        holder.binding.teamSerial.setText("#"+textNo);
        holder.binding.imgTick.setImageResource(R.drawable.ic_round);
        holder.binding.tvStatus.setText(lList.get(position).getMatchDate());

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
                holder.binding.tvStatus.setText(holder.binding.getRoot().getContext().getString(R.string.text_live));
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
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void AddData(LinkedList<UpcomingMatchModel> list) {
        lList = new LinkedList<>();
        lList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildUpcomingMatchBinding binding;

        public ViewHolder(@NonNull ChildUpcomingMatchBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
