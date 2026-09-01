package com.yorker.fanzania.views.screens.matchcontest.fragments.leagues;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildMcLeagueListBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.leagues.model.McLeagueModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.livescore.model.McLiveLeagueModel;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter.MyLeagueListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.model.LeagueTeamModel;

import java.util.List;

public class McLeagueListAdapter extends RecyclerView.Adapter<McLeagueListAdapter.ViewHolder> {

    private List<McLeagueModel> lList;
    private int userID;
    private ILeague callback;

    public McLeagueListAdapter(List<McLeagueModel> list, int user_ID,ILeague callback) {
        this.lList = list;
        this.userID = user_ID;
        this.callback = callback;
    }

    @NonNull
    @Override
    public McLeagueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildMcLeagueListBinding binding
                = ChildMcLeagueListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull McLeagueListAdapter.ViewHolder holder, int position) {

        if (lList.get(position).getTeamRank()>0)
            holder.binding.tvRank.setText(String.valueOf(lList.get(position).getTeamRank()));
        else
            holder.binding.tvRank.setText("-");

        if (lList.get(position).getLastMatchPoints()>0)
            holder.binding.tvMatchPoints.setText(String.valueOf(lList.get(position).getLastMatchPoints()));
        else
            holder.binding.tvMatchPoints.setText("-");

        if (lList.get(position).getUserId()==userID){
            callback.OnSetData(lList.get(position));
            holder.binding.clHeader.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorLightGrey));
        }
        else
            holder.binding.clHeader.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorWhite));

        holder.binding.tvTeam.setText(lList.get(position).getName());

        holder.binding.clHeader.setOnClickListener(view->{
            callback.OnClickLeagueTeam(lList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildMcLeagueListBinding binding;

        public ViewHolder(@NonNull ChildMcLeagueListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ILeague {
        void OnClickLeagueTeam(McLeagueModel leagueTeamModel);
        void OnSetData(McLeagueModel data);
    }
}
