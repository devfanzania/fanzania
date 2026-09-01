package com.yorker.fanzania.views.screens.tournament.liveleagueview.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildLeagueuserItemBinding;
import com.yorker.fanzania.views.screens.tournament.liveleagueview.model.LeagueUserModel;

import java.util.LinkedList;

public class LeagueUserAdapter extends RecyclerView.Adapter<LeagueUserAdapter.ViewHolder> {

    private IPoints callback = null;
    private LinkedList<LeagueUserModel> lList;
    private String userID;
    public static String myTeamID = null;

    public LeagueUserAdapter(LinkedList<LeagueUserModel> list, IPoints callback, String customerId) {
        this.lList = list;
        this.callback = callback;
        this.userID = customerId;
        myTeamID = null;
    }

    @NonNull
    @Override
    public LeagueUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildLeagueuserItemBinding childMyleagueItemBinding
                = ChildLeagueuserItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childMyleagueItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueUserAdapter.ViewHolder holder, int position) {

        if (userID.equals(String.valueOf(lList.get(position).getUserId())))
            holder.binding.llLeagueHeaders.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorGrey));
        else
            holder.binding.llLeagueHeaders.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorWhite));

        if (String.valueOf(lList.get(position).getUserId()).equalsIgnoreCase(userID)){
            myTeamID = String.valueOf(lList.get(position).getUserTeamId());
        }
        if (lList.get(position).getTeamNewStanding() > 0) {
            String txt;
            if (lList.get(position).getTeamOldStanding() > 0) {
                if (lList.get(position).getTeamNewStanding() < lList.get(position).getTeamOldStanding()) {
                    holder.binding.imgRank.setImageResource(R.drawable.ic_uparrow_green);
                    txt = "(+" + (lList.get(position).getTeamOldStanding() - lList.get(position).getTeamNewStanding()) + ")";
                } else if (lList.get(position).getTeamNewStanding() > lList.get(position).getTeamOldStanding()) {
                    holder.binding.imgRank.setImageResource(R.drawable.ic_downarrow_red);
                    txt = "(-" + (lList.get(position).getTeamNewStanding() - lList.get(position).getTeamOldStanding()) + ")";
                } else {
                    holder.binding.imgRank.setImageResource(R.drawable.ic_circle);
                    txt = "";
                }
            } else {
                holder.binding.imgRank.setImageResource(R.drawable.ic_circle);
                txt ="";
            }

            holder.binding.tvRank.setText(String.valueOf(lList.get(position).getTeamNewStanding()) + txt);
        } else
            holder.binding.tvRank.setText("-");

        holder.binding.tvTeamName.setText(lList.get(position).getUserTeamName());
        holder.binding.tvTeamOwnerName.setText(lList.get(position).getUserName());

        holder.binding.tvTransfer.setText(String.valueOf(lList.get(position).getTransfers()+"/"+lList.get(position).getTransfersUsed()));

        holder.binding.tvTotalPoints.setText(String.valueOf(lList.get(position).getTotalPoints()));
        holder.binding.tvPoints.setText(String.valueOf(lList.get(position).getCurrentMatchPoints()));

        if (lList.get(position).getPowerPlay()!=null && lList.get(position).getPowerPlay().equals("NA")) {
            holder.binding.imgPowerPlay.setVisibility(View.GONE);
        } else if (lList.get(position).getPowerPlay()!=null){
            holder.binding.imgPowerPlay.setVisibility(View.VISIBLE);
            switch (lList.get(position).getPowerPlay()) {
                case "NITRO":
                    holder.binding.imgPowerPlay.setImageResource(R.drawable.ic_new_nitro);
                    break;

                case "PAINKILLER":
                    holder.binding.imgPowerPlay.setImageResource(R.drawable.ic_new_painkiller);
                    break;

                case "AUTOCAPTAIN":
                    holder.binding.imgPowerPlay.setImageResource(R.drawable.ic_new_autocaptain);
                    break;
            }
        }


        holder.binding.llLeagueHeaders.setOnClickListener(view -> callback.OnClickLeaguTeam(lList.get(position)));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildLeagueuserItemBinding binding;

        public ViewHolder(@NonNull ChildLeagueuserItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IPoints {
        void OnClickLeaguTeam(LeagueUserModel data);
    }
}
