package com.yorker.fanzania.views.screens.matchcontest.fragments.livescore;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.MeChildLeagueItemBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.livescore.model.McLiveLeagueModel;

import java.util.List;

public class McLiveLeagueListAdapter extends RecyclerView.Adapter<McLiveLeagueListAdapter.ViewHolder> {

    private List<McLiveLeagueModel> lList;
    private ILeagues callback = null;
    private int userID;

    public McLiveLeagueListAdapter(ILeagues callback, List<McLiveLeagueModel> list, int user_ID) {
        this.callback = callback;
        this.lList = list;
        this.userID = user_ID;
    }

    @NonNull
    @Override
    public McLiveLeagueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(MeChildLeagueItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull McLiveLeagueListAdapter.ViewHolder holder, int position) {

        holder.binding.txtRank.setText(String.valueOf(lList.get(position).getTeamNewStanding()));
        holder.binding.txtMatchPoint.setText(String.valueOf(lList.get(position).getCurrentMatchPoints()));
        holder.binding.txtTeam.setText(lList.get(position).getUserName());

        if (lList.get(position).getUserId()==userID){
            holder.binding.clTab.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorLightGrey));
            callback.OnSetData(lList.get(position));
        }
        else
            holder.binding.clTab.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorWhite));

        holder.binding.clTab.setOnClickListener(view->callback.OnSelectLeague(lList.get(position)));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MeChildLeagueItemBinding binding;

        public ViewHolder(@NonNull MeChildLeagueItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ILeagues {
        void OnSelectLeague(McLiveLeagueModel data);
        void OnSetData(McLiveLeagueModel data);
    }
}
