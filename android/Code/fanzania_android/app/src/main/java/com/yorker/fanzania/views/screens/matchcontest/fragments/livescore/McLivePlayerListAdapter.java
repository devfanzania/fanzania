package com.yorker.fanzania.views.screens.matchcontest.fragments.livescore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.McChildLivePlayerItemBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;
import java.util.List;

public class McLivePlayerListAdapter extends RecyclerView.Adapter<McLivePlayerListAdapter.ViewHolder> {

    private List<LivePlayerModel> lList;

    public McLivePlayerListAdapter(List<LivePlayerModel> list) {
        this.lList = list;
    }

    @NonNull
    @Override
    public McLivePlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        McChildLivePlayerItemBinding childLivePlaylistItemBinding
                = McChildLivePlayerItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(childLivePlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull McLivePlayerListAdapter.ViewHolder holder, int position) {
        if (lList.get(position).isPlayerSelected())
            holder.binding.clTab2.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorGrey));
        else
            holder.binding.clTab2.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorWhite));

        holder.binding.txtPlayer.setText(lList.get(position).getPlayerName());
        holder.binding.txtBating.setText(String.valueOf(lList.get(position).getBattingPoints()));
        holder.binding.txtBowling.setText(String.valueOf(lList.get(position).getBowlingPoints()));
        holder.binding.txtFielding.setText(String.valueOf(lList.get(position).getFieldingPoints()));
        holder.binding.txtTotal.setText(String.valueOf(lList.get(position).getTotalPoints()));

        if (lList.get(position).isCapt()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
        } else if (lList.get(position).isvCapt()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
        } else
            holder.binding.imgCVC.setVisibility(View.GONE);

        if (lList.get(position).getPlayerType().equals("local"))
            holder.binding.imgOverseas.setVisibility(View.GONE);
        else
            holder.binding.imgOverseas.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private McChildLivePlayerItemBinding binding;

        public ViewHolder(@NonNull McChildLivePlayerItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
