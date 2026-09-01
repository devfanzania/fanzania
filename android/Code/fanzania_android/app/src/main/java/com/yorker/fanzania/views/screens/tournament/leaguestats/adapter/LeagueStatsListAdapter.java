package com.yorker.fanzania.views.screens.tournament.leaguestats.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.databinding.ChildLeagueStatsItemBinding;
import com.yorker.fanzania.views.screens.tournament.leaguestats.model.LeagueStatsModel;

import java.util.LinkedList;

public class LeagueStatsListAdapter extends RecyclerView.Adapter<LeagueStatsListAdapter.ViewHolder> {

    private Context context;
    private LinkedList<LeagueStatsModel> lList;
    private int tabPOsition;

    public LeagueStatsListAdapter(Context context, LinkedList<LeagueStatsModel> list, int position) {
        this.context = context;
        this.lList = list;
        this.tabPOsition = position;
    }

    @NonNull
    @Override
    public LeagueStatsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildLeagueStatsItemBinding binding
                = ChildLeagueStatsItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueStatsListAdapter.ViewHolder holder, int position) {

        holder.binding.tvRank.setText(String.valueOf(lList.get(position).getTeamRank()));
        holder.binding.tvTeamName.setText(lList.get(position).getUserTeamName());

        String players=lList.get(position).getPlayer1()+"\n"+lList.get(position).getPlayer2()+"\n"+lList.get(position).getPlayer3();
        holder.binding.tvTeamOwner.setText(players);

        switch (tabPOsition){
            case 0:
                String points=lList.get(position).getPlayer1Points()+"\n"+lList.get(position).getPlayer2Points()+"\n"+lList.get(position).getPlayer3Points();
                holder.binding.tvPoints.setText(points);
                break;

            case 1:
                String matchPoints=lList.get(position).getPlayer1Match()+"\n"+lList.get(position).getPlayer2Match()+"\n"+lList.get(position).getPlayer3Match();
                holder.binding.tvPoints.setText(matchPoints);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildLeagueStatsItemBinding binding;

        public ViewHolder(@NonNull ChildLeagueStatsItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}
