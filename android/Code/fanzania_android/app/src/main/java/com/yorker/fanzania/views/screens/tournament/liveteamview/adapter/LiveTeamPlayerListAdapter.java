package com.yorker.fanzania.views.screens.tournament.liveteamview.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildLivePlaylistItemNewBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;

import java.util.LinkedList;

public class LiveTeamPlayerListAdapter extends RecyclerView.Adapter<LiveTeamPlayerListAdapter.ViewHolder> {

    private Context mContext;
    private LinkedList<LivePlayerModel> lList;

    public LiveTeamPlayerListAdapter(Context context, LinkedList<LivePlayerModel> list) {
        this.mContext = context;
        this.lList = list;
    }

    @NonNull
    @Override
    public LiveTeamPlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildLivePlaylistItemNewBinding childLivePlaylistItemBinding
                = ChildLivePlaylistItemNewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childLivePlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveTeamPlayerListAdapter.ViewHolder holder, int position) {

        if(lList.get(position).isPlayerSelected())
            holder.binding.llHeader.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey));
        else
            holder.binding.llHeader.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));

        if (lList.get(position).isCapt()){
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
        }else if (lList.get(position).isvCapt()){
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
        } else
            holder.binding.imgCVC.setVisibility(View.GONE);

        holder.binding.tvPlayerName.setText(lList.get(position).getPlayerName());

        if (lList.get(position).getBattingPoints() == 0)
            holder.binding.tvBat.setText("-");
        else
            holder.binding.tvBat.setText(String.valueOf(lList.get(position).getBattingPoints()));

        if (lList.get(position).getBowlingPoints() == 0)
            holder.binding.tvBowl.setText("-");
        else
            holder.binding.tvBowl.setText(String.valueOf(lList.get(position).getBowlingPoints()));

        if (lList.get(position).getFieldingPoints() == 0)
            holder.binding.tvField.setText("-");
        else
            holder.binding.tvField.setText(String.valueOf(lList.get(position).getFieldingPoints()));

        if (lList.get(position).getTotalPoints() == 0)
            holder.binding.tvPoints.setText("-");
        else
            holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildLivePlaylistItemNewBinding binding;

        public ViewHolder(@NonNull ChildLivePlaylistItemNewBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
