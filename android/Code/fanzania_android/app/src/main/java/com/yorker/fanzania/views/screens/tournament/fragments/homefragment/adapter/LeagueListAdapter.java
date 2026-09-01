package com.yorker.fanzania.views.screens.tournament.fragments.homefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.databinding.ChildLeagueItemNewBinding;
import com.yorker.fanzania.databinding.ChildTopPlayersBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.TopPlayersResponse;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserLeagueModel;

import java.util.LinkedList;

public class LeagueListAdapter extends RecyclerView.Adapter<LeagueListAdapter.ViewHolder> {

    private Context context;

    private ILeague callback = null;

    private LinkedList<TopPlayersResponse> lList;

    public LeagueListAdapter(Context context, LinkedList<TopPlayersResponse> list, ILeague callback) {
        this.context = context;
        this.lList = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public LeagueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildTopPlayersBinding childLeagueItemBinding
                = ChildTopPlayersBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childLeagueItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueListAdapter.ViewHolder holder, int position) {
        holder.binding.tvRank.setText(String.valueOf(lList.get(position).getRank()));
        holder.binding.tvTeamName.setText(lList.get(position).getUserTeamName());
        holder.binding.tvTeamOwner.setText(lList.get(position).getName());
        holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getLastMatchPoints()));

        holder.binding.rrHeader.setOnClickListener(view -> {
            callback.onClickLeagueList(lList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void AddData(LinkedList<TopPlayersResponse> list) {
        lList=new LinkedList<>();
        lList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildTopPlayersBinding binding;

        public ViewHolder(@NonNull ChildTopPlayersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ILeague {

        void onClickLeagueList(TopPlayersResponse userLeagueModel);
    }
}
