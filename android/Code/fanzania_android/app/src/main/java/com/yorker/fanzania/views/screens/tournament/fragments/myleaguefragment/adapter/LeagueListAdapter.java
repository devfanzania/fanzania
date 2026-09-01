package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.CustomTab1Binding;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserLeagueModel;

import java.util.LinkedList;

public class LeagueListAdapter extends RecyclerView.Adapter<LeagueListAdapter.ViewHolder> {

    private Context context;

    private ILeague callback = null;

    private int prevPos = -1;
    private String leagueID;

    private LinkedList<UserLeagueModel> lList;

    public LeagueListAdapter(Context context, LinkedList<UserLeagueModel> list, Boolean isActiveTournament, ILeague iMatch, String leagueID) {
        this.context = context;
        this.lList = list;
        this.callback = iMatch;
        this.leagueID = leagueID;
    }

    @NonNull
    @Override
    public LeagueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        CustomTab1Binding binding
                = CustomTab1Binding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueListAdapter.ViewHolder holder, int position) {

        if (lList.get(position).getSelected()){
            changeBackgroundSelected(holder.binding.rrSub, holder.binding.tvLeagueName,holder.binding.tvLeagueRank,holder.binding.view);
        }
        else{
            changeBackgroundDeselected(holder.binding.rrSub, holder.binding.tvLeagueName,holder.binding.tvLeagueRank,holder.binding.view);
        }

        if (leagueID != null) {
            if (lList.get(position).getLeagueId().equals(leagueID)){
                if (prevPos==-1){
                    lList.get(position).setSelected(true);
                    prevPos = position;
                    changeBackgroundSelected(holder.binding.rrSub, holder.binding.tvLeagueName,holder.binding.tvLeagueRank,holder.binding.view);
                    callback.onClickLeagueList(lList.get(position));
                }
            }
        } else {
            if (position == 0 && prevPos==-1){
                lList.get(position).setSelected(true);
                prevPos = position;
                changeBackgroundSelected(holder.binding.rrSub, holder.binding.tvLeagueName,holder.binding.tvLeagueRank,holder.binding.view);
                callback.onClickLeagueList(lList.get(position));
            }
        }

        holder.binding.tvLeagueName.setText(lList.get(position).getLeagueName());

//        if (!lList.get(position).getLeagueRank().equals("0"))
//        {
//            holder.binding.tvLeagueRank.setVisibility(View.VISIBLE);
            holder.binding.tvLeagueRank.setText("Rank - "+lList.get(position).getLeagueRank());
//        }else
//            holder.binding.tvLeagueRank.setVisibility(View.GONE);

        holder.binding.rrSub.setOnClickListener(view -> {
            if (prevPos!=-1){
                if (prevPos!=position){
                    lList.get(position).setSelected(true);
                    lList.get(prevPos).setSelected(false);
                    prevPos = position;
                    callback.onClickLeagueList(lList.get(position));
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void changeBackgroundSelected(RelativeLayout rrSub, MontserratRegular tvLeagueName, MontserratRegular tvLeagueRank,View view){
        rrSub.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_red, context.getTheme()));

        tvLeagueName.setTextColor(context.getResources().getColor(R.color.colorWhite));
        tvLeagueRank.setTextColor(context.getResources().getColor(R.color.colorWhite));
        view.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
    }

    private void changeBackgroundDeselected(RelativeLayout rrSub, MontserratRegular tvLeagueName, MontserratRegular tvLeagueRank,View view){
        rrSub.setBackground(context.getResources().getDrawable(R.drawable.tab_round_corner_white, context.getTheme()));

        tvLeagueName.setTextColor(context.getResources().getColor(R.color.colorRedNew));
        tvLeagueRank.setTextColor(context.getResources().getColor(R.color.colorRedNew));
        view.setBackgroundColor(context.getResources().getColor(R.color.colorRedNew));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void AddData(LinkedList<UserLeagueModel> list) {
        prevPos=-1;
        lList = new LinkedList<>();
        lList.addAll(list);
        notifyDataSetChanged();
    }

    public void ClearData() {
        lList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTab1Binding binding;

        public ViewHolder(@NonNull CustomTab1Binding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ILeague {
        void onClickLeagueList(UserLeagueModel data);
    }
}
