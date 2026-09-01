package com.yorker.fanzania.views.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ItemPlayerCompareBinding;
import com.yorker.fanzania.databinding.ItemPlayerCompareBindingImpl;
import com.yorker.fanzania.databinding.McChildLiveTeamScoreItemBinding;
import com.yorker.fanzania.views.model.LiveTeamScoreComparison;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;

import java.util.List;

public class LiveMatchCompareAdapter extends RecyclerView.Adapter<LiveMatchCompareAdapter.ViewHolder> {

    private List<LiveTeamScoreComparison> lList;

    public LiveMatchCompareAdapter(List<LiveTeamScoreComparison> list) {
        this.lList = list;
    }

    @NonNull
    @Override
    public LiveMatchCompareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlayerCompareBinding childLivePlaylistItemBinding
                = ItemPlayerCompareBindingImpl.inflate(layoutInflater, parent, false);

        return new LiveMatchCompareAdapter.ViewHolder(childLivePlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveMatchCompareAdapter.ViewHolder holder, int position) {

        holder.binding.tvBatsmanFirst.setText(lList.get(position).getMyPlayerName());
        holder.binding.tvBatsmanSecond.setText(lList.get(position).getOtherPlayerName());
        holder.binding.tvPointsFirst.setText(""+lList.get(position).getMyTotalPoints());
        holder.binding.tvPointsSecond.setText(""+lList.get(position).getOtherTotalPoints());
        int pointDiff = lList.get(position).getMyTotalPoints() - lList.get(position).getOtherTotalPoints();

        if (lList.get(position).getMyVCapt()){
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
        }else if (lList.get(position).getMyCapt()){
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
        }else{
            holder.binding.imgCVC.setVisibility(View.GONE);
        }

        if (lList.get(position).getOtherCapt()){
            holder.binding.imgCVCO.setVisibility(View.VISIBLE);
            holder.binding.imgCVCO.setImageResource(R.drawable.ic_c);
        }else if (lList.get(position).getOtherVCapt()){
            holder.binding.imgCVCO.setVisibility(View.VISIBLE);
            holder.binding.imgCVCO.setImageResource(R.drawable.ic_vc);
        }else{
            holder.binding.imgCVCO.setVisibility(View.GONE);
        }

        if (lList.get(position).getMyPlayerSelected()){
            holder.binding.llFirst.setBackgroundColor(Color.parseColor("#EAEAEA"));
        }
        if (lList.get(position).getOtherPlayerSelected()){
            holder.binding.llSecond.setBackgroundColor(Color.parseColor("#EAEAEA"));
        }
        if (pointDiff == 0){
            holder.binding.tvPointDif.setText("-");
        }else{
            if (pointDiff > 0)
            {
                holder.binding.tvPointDif.setText("+ "+pointDiff);
                holder.binding.tvPointDif.setTextColor(Color.parseColor("#497804"));

            }else{
                holder.binding.tvPointDif.setText(""+pointDiff);
                holder.binding.tvPointDif.setTextColor(Color.parseColor("#E95354"));
            }
        }

//        holder.binding.tvPlayerName.setText(lList.get(position).getPlayerName());
//        holder.binding.tvBat.setText(String.valueOf(lList.get(position).getBattingPoints()));
//        holder.binding.tvBowl.setText(String.valueOf(lList.get(position).getBowlingPoints()));
//        holder.binding.tvField.setText(String.valueOf(lList.get(position).getFieldingPoints()));
//        holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));
//
//        if (lList.get(position).isCapt()) {
//            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
//        } else if (lList.get(position).isvCapt()) {
//            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
//        } else
//            holder.binding.imgCVC.setImageResource(0);
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPlayerCompareBinding binding;

        public ViewHolder(@NonNull ItemPlayerCompareBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}

