package com.yorker.fanzania.views.screens.tournament.teamstats.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yorker.fanzania.databinding.ChildTeamStatsItemBinding;
import com.yorker.fanzania.views.screens.tournament.teamstats.model.UserStatsModel;

import java.util.LinkedList;

public class TeamStatsListAdapter extends RecyclerView.Adapter<TeamStatsListAdapter.ViewHolder> {

    private Context context;
    private LinkedList<UserStatsModel> lList;
    private int tabPOsition;

    public TeamStatsListAdapter(Context context, LinkedList<UserStatsModel> list, int position) {
        this.context = context;
        this.lList = list;
        this.tabPOsition = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildTeamStatsItemBinding binding
                = ChildTeamStatsItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (tabPOsition){
            case 0:
                holder.binding.tvRank.setText(String.valueOf(lList.get(position).getPlayerRank()));
                holder.binding.tvTeamName.setText(lList.get(position).getPlayerShortName());
                holder.binding.tvTeamOwner.setText(lList.get(position).getParticipationTeamName());
                holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));
                break;

            case 1:
                holder.binding.tvRank.setText(String.valueOf(lList.get(position).getMatchNo()));
                String text=lList.get(position).getTeam1()+" vs "+lList.get(position).getTeam2();
                holder.binding.tvTeamName.setText(text);
                holder.binding.tvTeamOwner.setText(lList.get(position).getCaptain());
                holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildTeamStatsItemBinding binding;

        public ViewHolder(@NonNull ChildTeamStatsItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}
