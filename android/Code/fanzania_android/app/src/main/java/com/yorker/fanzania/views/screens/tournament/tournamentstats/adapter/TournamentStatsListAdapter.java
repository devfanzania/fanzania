package com.yorker.fanzania.views.screens.tournament.tournamentstats.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildTeamStatsItemBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserLeagueModel;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter.LeagueListAdapter;
import com.yorker.fanzania.views.screens.tournament.tournamentstats.model.TournamentStatsModel;

import java.util.LinkedList;

public class TournamentStatsListAdapter extends RecyclerView.Adapter<TournamentStatsListAdapter.ViewHolder> {

    private Context context;
    private LinkedList<TournamentStatsModel> lList;
    private int tabPOsition;
    private ICallback callback = null;

    public TournamentStatsListAdapter(Context context, LinkedList<TournamentStatsModel> list, int position, ICallback callback) {
        this.context = context;
        this.lList = list;
        this.tabPOsition = position;
        this.callback = callback;
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
                holder.binding.tvPoints.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0);
                break;

            case 1:
                holder.binding.tvRank.setText(String.valueOf(lList.get(position).getTeamRank()));
                holder.binding.tvTeamName.setText(lList.get(position).getUserTeamName());
                holder.binding.tvTeamOwner.setText(lList.get(position).getOwner());
                holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));
                holder.binding.tvPoints.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_right_arrow_new,0);
                break;

            case 2:
                holder.binding.tvRank.setText(String.valueOf(lList.get(position).getLeagueRank()));
                holder.binding.tvTeamName.setText(lList.get(position).getLeagueName());
                holder.binding.tvTeamOwner.setText(lList.get(position).getLeagueOwner());
                holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getLeaguePoints()));
                holder.binding.tvPoints.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0);
                break;
        }

        holder.binding.rlMain.setOnClickListener(v->{
            if (tabPOsition==1)
            {
                callback.onClickMatch(lList.get(position));
            }
        });
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

    public interface ICallback {
        void onClickMatch(TournamentStatsModel data);
    }
}
