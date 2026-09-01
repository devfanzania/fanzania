package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.databinding.ChildLiveLeaguelistItemBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserLeagueModel;

import java.util.LinkedList;

public class LiveLeagueListAdapter extends RecyclerView.Adapter<LiveLeagueListAdapter.ViewHolder> {

    private Context context;
    private LinkedList<UserLeagueModel> lList;
    private ILeagues callback = null;

    public LiveLeagueListAdapter(Context context, ILeagues callback, LinkedList<UserLeagueModel> list) {
        this.context = context;
        this.callback = callback;
        this.lList = list;
    }

    @NonNull
    @Override
    public LiveLeagueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildLiveLeaguelistItemBinding childLivePlaylistItemBinding
                = ChildLiveLeaguelistItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childLivePlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveLeagueListAdapter.ViewHolder holder, int position) {
        holder.binding.leagueName.setText(lList.get(position).getLeagueName());
        holder.binding.leagueOwner.setText(lList.get(position).getLeagueLeader());
        holder.binding.llHeaderL.setOnClickListener(view->callback.OnSelectLeague(lList.get(position)));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildLiveLeaguelistItemBinding binding;

        public ViewHolder(@NonNull ChildLiveLeaguelistItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ILeagues {
        void OnSelectLeague(UserLeagueModel data);
    }
}
