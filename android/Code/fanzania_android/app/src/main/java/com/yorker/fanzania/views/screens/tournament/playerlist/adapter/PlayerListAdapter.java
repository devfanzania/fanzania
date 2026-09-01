package com.yorker.fanzania.views.screens.tournament.playerlist.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ChildPlaylistItemBinding;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;
import com.yorker.fanzania.widgets.ViewBinderHelper;

import java.util.LinkedList;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> implements Filterable {

    private Context context;
    private final CustomFilter mFilter;
    private IPoints callback;
    private final LinkedList<PlayerDataType> filteredList = new LinkedList<>();
    private LinkedList<PlayerDataType> dictionaryWords;
    private LinkedList<PlayerDataType> lList;
    private int playerCount;
    private int remainingBudget;

    private int MaxWicketKeeper;
    private int MaxBatsman;
    private int MaxBowler;
    private int MaxAllrounder;

    private int batsmanCount;
    private int bolwerCount;
    private int allrunderCount;
    private int wicketKeeperCount;

    private ViewBinderHelper binderHelper ;
    ItemClickListener itemClickListener;

    public interface ItemClickListener{
        public void onPlayerClick(PlayerDataType playerId);
    }

    public PlayerListAdapter(Context context, LinkedList<PlayerDataType> list,
                             IPoints callback, int count, int budget, int maxWicketKeeper,
                             int maxBowler, int maxBatsMan, int maxAllRounder,
                             int wicketKeeperCount, int bolwerCount, int batsmanCount, int allrunderCount, ItemClickListener itemClickListener
                             ) {
        this.context = context;
        this.lList = list;
        dictionaryWords=lList;
        this.playerCount = count;
        this.callback = callback;

        this.remainingBudget = budget;

        this.MaxWicketKeeper = maxWicketKeeper;

        this.MaxBatsman = maxBatsMan;

        this.MaxBowler = maxBowler;

        this.MaxAllrounder = maxAllRounder;

        this.wicketKeeperCount = wicketKeeperCount;

        this.bolwerCount = bolwerCount;

        this.batsmanCount = batsmanCount;

        this.allrunderCount = allrunderCount;

        this.itemClickListener = itemClickListener;

        mFilter = new CustomFilter(PlayerListAdapter.this);

        binderHelper= new ViewBinderHelper();
    }

    @NonNull
    @Override
    public PlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildPlaylistItemBinding childPlaylistItemBinding
                = ChildPlaylistItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(childPlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.ViewHolder holder, int position) {

        binderHelper.bind(holder.binding.swipeLayout, String.valueOf(position));

        if (lList.get(position).isPlayingInd())
            holder.binding.imgIsPlaying.setVisibility(View.VISIBLE);
        else
            holder.binding.imgIsPlaying.setVisibility(View.GONE);

        holder.binding.tvRank.setText(String.valueOf(lList.get(position).getTotalPoints()));
        holder.binding.tvPlayerName.setText(lList.get(position).getPlayerShortName());
        holder.binding.tvTeam.setText(lList.get(position).getTeamShortName());

        String text = lList.get(position).getPlayerValue() + "k";
        holder.binding.tvPoints.setText(text);

        if (lList.get(position).getPlayerType().equals("overseas"))
            holder.binding.tvPlayerName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_overseas,0);
        else
            holder.binding.tvPlayerName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

        switch (lList.get(position).getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                holder.binding.tvPlayerType.setImageResource(R.drawable.ic_new_batsman);
                break;
            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                holder.binding.tvPlayerType.setImageResource(R.drawable.ic_new_allrounder);
                break;
            case Constants.TAG_PLAYERTYPE_BLOWER:
                holder.binding.tvPlayerType.setImageResource(R.drawable.ic_new_bowler);
                break;
            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                holder.binding.tvPlayerType.setImageResource(R.drawable.ic_new_keeper);
                break;
        }

        holder.binding.rrHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener !=null){
                    itemClickListener.onPlayerClick(lList.get(position));
                }
            }
        });

        holder.binding.llAdd.setOnClickListener(view -> {
                if (remainingBudget >= lList.get(position).getPlayerValue()) {
                    System.out.println("playercount adapter "+playerCount);
                    if (playerCount < 11) {
                        switch (lList.get(position).getPlayerSpeciality()) {
                            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                                if (wicketKeeperCount < MaxWicketKeeper)
                                    setPlayerAsSelected(lList.get(position));
                                else
                                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_wicketkeeperlimitexceed));
                                break;

                            case Constants.TAG_PLAYERTYPE_BATSMAN:
                                if (batsmanCount < MaxBatsman)
                                    setPlayerAsSelected(lList.get(position));
                                else
                                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_batsmanlimitexceed));
                                break;

                            case Constants.TAG_PLAYERTYPE_BLOWER:
                                if (bolwerCount < MaxBowler)
                                    setPlayerAsSelected(lList.get(position));
                                else
                                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_bowlerlimitexceed));
                                break;

                            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                                if (allrunderCount < MaxAllrounder)
                                    setPlayerAsSelected(lList.get(position));
                                else
                                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_allrounderlimitexceed));
                                break;
                        }
                    } else
                        CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_playerselectionlimit));
                } else
                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_outofbudget));
        });
    }

    private void setPlayerAsSelected(PlayerDataType playerDataType) {
        callback.OnSelectPlayer(playerDataType);
    }

    public void removePlayer(PlayerDataType playerDataType){
        playerCount++;
        lList.remove(lList.indexOf(playerDataType));
        notifyDataSetChanged();
    }

    public void refreshBudget(int value) {
       this.remainingBudget=value;
    }

    public void sortData(LinkedList<PlayerDataType> pList) {
        this.lList = pList;
        dictionaryWords=lList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, lList.size());
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void removeSelectedPlayer(PlayerDataType data, int count) {
        this.playerCount = count;
        lList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void refreshCounts(int batsmanCount, int bolwerCount, int allrunderCount, int wicketKeeperCount) {
        this.wicketKeeperCount=wicketKeeperCount;
        this.bolwerCount=bolwerCount;
        this.batsmanCount=batsmanCount;
        this.allrunderCount=allrunderCount;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildPlaylistItemBinding binding;

        public ViewHolder(@NonNull ChildPlaylistItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IPoints {
        void OnSelectPlayer(PlayerDataType playerDataType);
    }

    public class CustomFilter extends Filter {
        private final PlayerListAdapter mAdapter;

        CustomFilter(PlayerListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0)
                filteredList.addAll(dictionaryWords);
            else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final PlayerDataType mWords : dictionaryWords) {
                    if (mWords.getPlayerName().toLowerCase().contains(filterPattern))
                        filteredList.add(mWords);
                }
            }
            results.values = filteredList;
            lList = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
