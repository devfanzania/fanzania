package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildMatchcontestTeamItemBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LiveMatchModel;
import com.yorker.fanzania.widgets.DateUtils;

import java.util.List;

public class NewMatchListAdapter extends RecyclerView.Adapter<NewMatchListAdapter.ViewHolder> {

    private IMatch callback = null;
    private int prevPos = -1;
    private List<LiveMatchModel> lList;
    private int cMatchID;

    public NewMatchListAdapter(List<LiveMatchModel> list, IMatch iMatch, int matchID) {
        this.lList = list;
        this.callback = iMatch;
        this.cMatchID = matchID;
    }

    @NonNull
    @Override
    public NewMatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildMatchcontestTeamItemBinding binding
                = ChildMatchcontestTeamItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMatchListAdapter.ViewHolder holder, int position) {

        holder.binding.tvTeamName.setText(new StringBuilder().append(lList.get(position).getTeam1ShortName())
                .append(" v ")
                .append(lList.get(position).getTeam2ShortName()));

        switch (lList.get(position).getMatchStatus()) {
            case "COMPLETE":
                holder.binding.imgTick.setVisibility(View.VISIBLE);
                holder.binding.imgTick.setImageResource(R.drawable.ic_check_black);
                holder.binding.tvStatus.setText(lList.get(position).getMatchDate());
                break;

            case "Live":
                holder.binding.imgTick.setVisibility(View.VISIBLE);
                holder.binding.imgTick.setImageResource(R.drawable.ic_round);
                holder.binding.tvStatus.setText(holder.binding.getRoot().getContext().getString(R.string.text_live));
                break;

            default:
                holder.binding.imgTick.setVisibility(View.GONE);
                holder.binding.tvStatus.setText(lList.get(position).getMatchDate());
                break;
        }

//        switch (lList.get(position).getMatchStatus()) {
//            case "COMPLETE":
//                holder.binding.imgTick.setVisibility(View.VISIBLE);
//                holder.binding.imgTick.setImageResource(R.drawable.ic_check_black);
//                DateUtils.getDateFromISO(lList.get(position).getMatchDate(), holder.binding.tvStatus);
//                break;
//
//            case "Live":
//                holder.binding.imgTick.setVisibility(View.VISIBLE);
//                holder.binding.imgTick.setImageResource(R.drawable.ic_round);
//                DateUtils.getDateFromISO(lList.get(position).getMatchDate(), holder.binding.tvStatus);
//                break;
//
//            default:
//                holder.binding.imgTick.setVisibility(View.GONE);
//                DateUtils.getDateFromISO(lList.get(position).getMatchDate(), holder.binding.tvStatus);
//                break;
//        }

        if (lList.get(position).isSelected()) {
            prevPos=position;
            holder.binding.rlMain.setBackground(holder.binding.getRoot().getContext().getResources().getDrawable(R.drawable.tab_round_corner_red,
                    holder.binding.getRoot().getContext().getTheme()));
            callback.onClickMatchList(lList.get(position));
        } else {
            holder.binding.rlMain.setBackground(holder.binding.getRoot().getContext().getResources().getDrawable(R.drawable.tab_round_corner_dark,
                    holder.binding.getRoot().getContext().getTheme()));
        }

        if (cMatchID != 0 && cMatchID == lList.get(position).getMatchId()) {
//            isMatchSelected = true;
            cMatchID = 0;
            if (prevPos == -1) {
                lList.get(0).setSelected(false);
                prevPos = position;
                lList.get(position).setSelected(true);
            } else {
                lList.get(position).setSelected(true);
                lList.get(prevPos).setSelected(false);
                prevPos = position;
            }
            callback.onClickMatchList(lList.get(position));
            holder.binding.rlMain.setBackground(holder.binding.getRoot().getContext().getResources().getDrawable(R.drawable.tab_round_corner_red,
                    holder.binding.getRoot().getContext().getTheme()));
        }

        holder.binding.rlMain.setOnClickListener(view -> {
            if (prevPos > -1) {
                if (prevPos != position) {
                    lList.get(position).setSelected(true);
                    lList.get(prevPos).setSelected(false);
                    prevPos = position;
                }
            } else {
                lList.get(position).setSelected(true);
                prevPos = position;
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildMatchcontestTeamItemBinding binding;

        public ViewHolder(@NonNull ChildMatchcontestTeamItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IMatch {
        void onClickMatchList(LiveMatchModel matches);
    }
}
