package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ChildLivePlaylistItemBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;

import java.util.LinkedList;

public class LivePlayerListAdapter extends RecyclerView.Adapter<LivePlayerListAdapter.ViewHolder> {

    private Context context;
    private LinkedList<LivePlayerModel> lList;

    public LivePlayerListAdapter(Context context, LinkedList<LivePlayerModel> list) {
        this.context = context;
        this.lList = list;
    }

    @NonNull
    @Override
    public LivePlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildLivePlaylistItemBinding childLivePlaylistItemBinding
                = ChildLivePlaylistItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(childLivePlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LivePlayerListAdapter.ViewHolder holder, int position) {
        if (lList.get(position).isPlayerSelected())
            holder.binding.rrHeader.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorGrey));
        else
            holder.binding.rrHeader.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorWhite));

        if (lList.get(position).isCapt())
        {
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
        }else if (lList.get(position).isvCapt())
        {
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
        }else
            holder.binding.imgCVC.setVisibility(View.INVISIBLE);

        holder.binding.tvPlayerName.setText(lList.get(position).getPlayerName());
        holder.binding.tvBat.setText(String.valueOf(lList.get(position).getBattingPoints()));
        holder.binding.tvBowl.setText(String.valueOf(lList.get(position).getBowlingPoints()));
        holder.binding.tvField.setText(String.valueOf(lList.get(position).getFieldingPoints()));
        holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));

        switch (lList.get(position).getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                holder.binding.imgType.setImageResource(R.drawable.ic_bat);
                break;
            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                holder.binding.imgType.setImageResource(R.drawable.ic_allrounder);
                break;
            case Constants.TAG_PLAYERTYPE_BLOWER:
                holder.binding.imgType.setImageResource(R.drawable.ic_bowler);
                break;
            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                holder.binding.imgType.setImageResource(R.drawable.ic_gloves);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildLivePlaylistItemBinding binding;

        public ViewHolder(@NonNull ChildLivePlaylistItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
