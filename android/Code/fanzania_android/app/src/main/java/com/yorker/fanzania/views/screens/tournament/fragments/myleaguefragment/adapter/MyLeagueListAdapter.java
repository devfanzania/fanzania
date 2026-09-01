package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ChildMyleagueItemNewBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.model.LeagueTeamModel;

import java.util.LinkedList;

public class MyLeagueListAdapter extends RecyclerView.Adapter<MyLeagueListAdapter.ViewHolder> {

    private Context context;

    private IPoints callback;
    private Boolean isUserAdmin;
    private String tournamentStatus;
    private String userID;
    private LinkedList<LeagueTeamModel> lList;

    public MyLeagueListAdapter(Context context, LinkedList<LeagueTeamModel> list, IPoints callback, Boolean isUserAdmin,
                               String tournamentStatus, String userID) {
        this.context = context;
        this.lList = list;
        this.callback = callback;
        this.isUserAdmin = isUserAdmin;
        this.tournamentStatus = tournamentStatus;
        this.userID = userID;
    }

    @NonNull
    @Override
    public MyLeagueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildMyleagueItemNewBinding childMyleagueItemBinding
                = ChildMyleagueItemNewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childMyleagueItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLeagueListAdapter.ViewHolder holder, int position) {

        if (lList.get(position).getIsLeagueLeader().equals("Y")) {
            holder.binding.tvAdd.setVisibility(View.GONE);
            holder.binding.tvDelete.setVisibility(View.GONE);
            holder.binding.img.setVisibility(View.VISIBLE);
            holder.binding.img.setImageResource(R.drawable.admin);
        } else {
            if (isUserAdmin) {
                switch (lList.get(position).getStatus()) {
                    case "Pending":
                        holder.binding.tvAdd.setVisibility(View.VISIBLE);
                        holder.binding.tvDelete.setVisibility(View.VISIBLE);
                        holder.binding.img.setVisibility(View.VISIBLE);
                        holder.binding.img.setImageResource(R.drawable.ic_pending);
                        break;

                    case "Approved":
                        holder.binding.tvAdd.setVisibility(View.GONE);
                        holder.binding.img.setVisibility(View.GONE);
                        holder.binding.tvDelete.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                holder.binding.tvAdd.setVisibility(View.GONE);
                holder.binding.tvDelete.setVisibility(View.GONE);
                holder.binding.img.setVisibility(View.GONE);
            }
        }

        holder.binding.tvTeamName.setText(lList.get(position).getUserTeamName());
        holder.binding.tvOwnerName.setText(lList.get(position).getFullName());
        switch (lList.get(position).getUserTier()) {
            case 1:
//                holder.binding.img1.setBackgroundResource(R.drawable.bronze_round_circle);
                break;

            case 2:
                holder.binding.img1.setBackgroundResource(R.drawable.silver);
                break;

            case 3:
                holder.binding.img1.setBackgroundResource(R.drawable.gold);
                break;

            case 4:
                holder.binding.img1.setBackgroundResource(R.drawable.platinum);
                break;

        }
        holder.binding.tvTransferPending.setText(lList.get(position).getSubsLeft());

        holder.binding.tvPoints.setText(lList.get(position).getTotalPoints());

        Picasso.get().load(Constants.BASE_IMAGE_URL+""+lList.get(position).getSupportedTeam()).into(holder.binding.imgTeam);
        if (lList.get(position).getLastMatchPoints() == 0)
            holder.binding.tvLastMatch.setText("0");
        else
            holder.binding.tvLastMatch.setText(String.valueOf(lList.get(position).getLastMatchPoints()));

        if (lList.get(position).getTeamCurrentStanding() > 0) {
            String txt;
            if (lList.get(position).getTeamOldStanding() > 0) {
                if (lList.get(position).getTeamCurrentStanding() < lList.get(position).getTeamOldStanding()) {
                    holder.binding.imgRank.setImageResource(R.drawable.ic_uparrow_green);
                    txt = "(+" + (lList.get(position).getTeamOldStanding() - lList.get(position).getTeamCurrentStanding()) + ")";
                } else if (lList.get(position).getTeamCurrentStanding() > lList.get(position).getTeamOldStanding()) {
                    holder.binding.imgRank.setImageResource(R.drawable.ic_downarrow_red);
                    txt = "(-" + (lList.get(position).getTeamCurrentStanding() - lList.get(position).getTeamOldStanding()) + ")";
                } else {
                    holder.binding.imgRank.setImageResource(R.drawable.ic_circle);
                    txt = "";
                }
            } else {
                holder.binding.imgRank.setImageResource(R.drawable.ic_circle);
                txt = "";
            }

            String text = String.valueOf(lList.get(position).getTeamCurrentStanding()) + txt;

            holder.binding.tvRank.setText(text);
        } else {
            holder.binding.imgRank.setImageResource(0);
            holder.binding.tvRank.setText("0");
        }

        if (lList.get(position).getUserId().equals(userID)) {
            holder.binding.rrHeader.setBackgroundColor(context.getResources().getColor(R.color.colorLightGrey));
            holder.binding.tvOwnerName.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.tvPoints.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.tvRank.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.tvTeamName.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.tvLastMatch.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.tvTransferPending.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.imgInfinity.setImageResource(R.drawable.ic_infinitydark);
        } else {
            holder.binding.rrHeader.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.binding.tvOwnerName.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.binding.tvPoints.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.binding.tvRank.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.binding.tvTeamName.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.binding.tvLastMatch.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.binding.tvTransferPending.setTextColor(context.getResources().getColor(R.color.colorText));
            holder.binding.imgInfinity.setImageResource(R.drawable.ic_infinitydark);
        }

        holder.binding.tvAdd.setOnClickListener(view -> callback.OnClickAddTeam(position, lList.get(position).getUserId()));

        holder.binding.tvDelete.setOnClickListener(view -> callback.OnClickRemoveTeam(lList.get(position).getUserLeagueId(), position));

        holder.binding.tvView.setOnClickListener(view -> callback.OnClickLeaguTeam(lList.get(position)));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void ClearALL() {
        lList.clear();
        notifyDataSetChanged();
    }

    public void TeamAdded(int position) {
        lList.get(position).setStatus("Approved");
        notifyDataSetChanged();
    }

    public void TeamRemoved(int position) {
        lList.remove(lList.get(position));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildMyleagueItemNewBinding binding;

        public ViewHolder(@NonNull ChildMyleagueItemNewBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            if (tournamentStatus.equals(Constants.TAG_INPROGRESS)) {
                binding.tvTransferPending.setVisibility(View.VISIBLE);
                binding.imgInfinity.setVisibility(View.GONE);
            } else {
                binding.tvTransferPending.setVisibility(View.GONE);
                binding.imgInfinity.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface IPoints {
        void OnClickLeaguTeam(LeagueTeamModel leagueTeamModel);

        void OnClickAddTeam(int position, String userId);

        void OnClickRemoveTeam(int UserLeagueId, int position);
    }
}
