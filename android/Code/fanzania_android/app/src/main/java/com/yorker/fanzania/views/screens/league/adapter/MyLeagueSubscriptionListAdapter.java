package com.yorker.fanzania.views.screens.league.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ChildMyleagueItemNewBinding;
import com.yorker.fanzania.databinding.ItemLeagueSubscriptionTeamBinding;
import com.yorker.fanzania.views.screens.league.LeagueSubscriptionModel;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.model.LeagueTeamModel;

import java.util.HashMap;
import java.util.LinkedList;

public class MyLeagueSubscriptionListAdapter extends RecyclerView.Adapter<MyLeagueSubscriptionListAdapter.ViewHolder> {

    private Context context;
    private IPoints callback;
    private String userID;
    private LinkedList<LeagueSubscriptionModel> lList;
    HashMap<Integer,ViewHolder> holderlist;

    public MyLeagueSubscriptionListAdapter(Context context, LinkedList<LeagueSubscriptionModel> list, IPoints callback) {
        this.context = context;
        this.lList = list;
        this.callback = callback;
        holderlist = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemLeagueSubscriptionTeamBinding childMyleagueItemBinding
                = ItemLeagueSubscriptionTeamBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childMyleagueItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(!holderlist.containsKey(position)){
            holderlist.put(position,holder);
        }
        LeagueSubscriptionModel leagueSubscriptionModel = lList.get(position);
        holder.binding.tvTeamName.setText(leagueSubscriptionModel.getUserTeamName());
        holder.binding.tvName.setText(leagueSubscriptionModel.getName());

        if (leagueSubscriptionModel.getSubscriptionType() == 1){
            holder.binding.imgPlan1.setVisibility(View.VISIBLE);
            holder.binding.imgPlan1.setClickable(false);
            holder.binding.imgPlan1.setEnabled(false);
            holder.binding.imgPlan1.setBackgroundResource(R.drawable.tick_grey);
            holder.binding.tvPlan1.setVisibility(View.GONE);
            holder.binding.tvPlan2.setText("Select");
            holder.binding.tvPlan3.setText("Select");

            //
//            holder.binding.imgPlan3.setClickable(false);
//            holder.binding.imgPlan3.setEnabled(false);
        }else if (leagueSubscriptionModel.getSubscriptionType() == 2){
            holder.binding.imgPlan2.setVisibility(View.VISIBLE);
            holder.binding.imgPlan2.setClickable(false);
            holder.binding.imgPlan2.setEnabled(false);
            holder.binding.imgPlan2.setBackgroundResource(R.drawable.tick_grey);
            holder.binding.tvPlan2.setVisibility(View.GONE);
            holder.binding.tvPlan1.setText("Select");
            holder.binding.tvPlan3.setText("Select");
        }else if (leagueSubscriptionModel.getSubscriptionType() == 3){
            holder.binding.imgPlan3.setVisibility(View.VISIBLE);
            holder.binding.imgPlan3.setClickable(false);
            holder.binding.imgPlan3.setEnabled(false);
            holder.binding.imgPlan3.setBackgroundResource(R.drawable.tick_grey);
            holder.binding.tvPlan3.setVisibility(View.GONE);
            holder.binding.tvPlan1.setText("-");
            holder.binding.tvPlan2.setText("-");
        }else{
            holder.binding.tvPlan1.setText("Select");
            holder.binding.tvPlan2.setText("Select");
            holder.binding.tvPlan3.setText("Select");
        }

        holder.binding.imgPlan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null){
                    callback.OnClicked(leagueSubscriptionModel, 1, holder.binding.tvTotal, holder.binding.imgPlan1, holder.binding.tvPlan1, holder.binding);
                }
            }
        });
        holder.binding.imgPlan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.OnClicked(leagueSubscriptionModel, 2, holder.binding.tvTotal, holder.binding.imgPlan2, holder.binding.tvPlan2, holder.binding);
            }
        });
        holder.binding.imgPlan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.OnClicked(leagueSubscriptionModel, 3, holder.binding.tvTotal, holder.binding.imgPlan3, holder.binding.tvPlan3, holder.binding);
            }
        });

        holder.binding.tvPlan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null){
                    callback.OnClicked(leagueSubscriptionModel, 1, holder.binding.tvTotal, holder.binding.imgPlan1, holder.binding.tvPlan1, holder.binding);
                }
            }
        });
        holder.binding.tvPlan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.OnClicked(leagueSubscriptionModel, 2, holder.binding.tvTotal, holder.binding.imgPlan2, holder.binding.tvPlan2, holder.binding);
            }
        });
        holder.binding.tvPlan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.OnClicked(leagueSubscriptionModel, 3, holder.binding.tvTotal, holder.binding.imgPlan3, holder.binding.tvPlan3, holder.binding);
            }
        });

        Log.e("data", ""+leagueSubscriptionModel.getName());
    }

    public ViewHolder getViewByPosition(int position) {
        return holderlist.get(position);
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLeagueSubscriptionTeamBinding binding;

        public ViewHolder(@NonNull ItemLeagueSubscriptionTeamBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IPoints {
        void OnClicked(LeagueSubscriptionModel leagueSubscriptionModel, int plan, TextView tvTotal, ImageView imageView, TextView tvPlan, ItemLeagueSubscriptionTeamBinding binding);
    }
}
