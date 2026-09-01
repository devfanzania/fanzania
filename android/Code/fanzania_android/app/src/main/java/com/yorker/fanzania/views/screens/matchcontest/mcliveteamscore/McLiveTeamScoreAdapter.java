package com.yorker.fanzania.views.screens.matchcontest.mcliveteamscore;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.McChildLiveTeamScoreItemBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;

import java.util.List;

public class McLiveTeamScoreAdapter extends RecyclerView.Adapter<McLiveTeamScoreAdapter.ViewHolder> {

    private List<LivePlayerModel> lList;

    public McLiveTeamScoreAdapter(List<LivePlayerModel> list) {
        this.lList = list;
    }

    @NonNull
    @Override
    public McLiveTeamScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        McChildLiveTeamScoreItemBinding childLivePlaylistItemBinding
                = McChildLiveTeamScoreItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(childLivePlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull McLiveTeamScoreAdapter.ViewHolder holder, int position) {

        holder.binding.tvPlayerName.setText(lList.get(position).getPlayerName());
        holder.binding.tvBat.setText(String.valueOf(lList.get(position).getBattingPoints()));
        holder.binding.tvBowl.setText(String.valueOf(lList.get(position).getBowlingPoints()));
        holder.binding.tvField.setText(String.valueOf(lList.get(position).getFieldingPoints()));
        holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));

        if (lList.get(position).isCapt()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
        } else if (lList.get(position).isvCapt()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
        } else
            holder.binding.imgCVC.setImageResource(0);
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private McChildLiveTeamScoreItemBinding binding;

        public ViewHolder(@NonNull McChildLiveTeamScoreItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
