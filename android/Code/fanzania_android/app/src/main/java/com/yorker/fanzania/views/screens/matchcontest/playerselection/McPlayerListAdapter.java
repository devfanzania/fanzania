package com.yorker.fanzania.views.screens.matchcontest.playerselection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.McChildPlaylistItemBinding;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;
import com.yorker.fanzania.views.screens.tournament.playerlist.adapter.PlayerListAdapter;
import com.yorker.fanzania.widgets.ViewBinderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class McPlayerListAdapter extends RecyclerView.Adapter<McPlayerListAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private final CustomFilter mFilter;
    private IPoints callback;
    private final LinkedList<PlayerListModel> filteredList = new LinkedList<>();
    private List<PlayerListModel> selectedList = new ArrayList();
    private LinkedList<PlayerListModel> dictionaryWords;
    private LinkedList<PlayerListModel> lList;
    private int playerCount;
    private int remainingBudget = -1;

    private int MaxWicketKeeper;
    private int MaxBatsman;
    private int MaxBowler;
    private int MaxAllrounder;
    private int MaxSameTeamPlayer;
    private int MaxOverseasPlayer;

    private int batsmanCount;
    private int bolwerCount;
    private int allrunderCount;
    private int wicketKeeperCount;
    private int captID = 0;
    private int viceCaptID = 0;
    private int prevCapt = -1;
    private int prevViceCapt = -1;
    private ViewBinderHelper binderHelper;

    ItemClickListener itemClickListener;
    public interface ItemClickListener{
        public void onPlayerClick(PlayerListModel playerId);
    }

    public McPlayerListAdapter(Context context, LinkedList<PlayerListModel> list,
                               IPoints callback, int count, int budget, int maxWicketKeeper,
                               int maxBowler, int maxBatsMan, int maxAllRounder,
                               int wicketKeeperCount, int bolwerCount, int batsmanCount, int allrunderCount, int sameTeamCount, int overseasount, ItemClickListener itemClickListener) {

        this.context = context;
        this.lList = list;
        dictionaryWords = lList;
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
        this.MaxOverseasPlayer = overseasount;
        this.MaxSameTeamPlayer = sameTeamCount;

        mFilter = new CustomFilter(McPlayerListAdapter.this);
        binderHelper = new ViewBinderHelper();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public McPlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        McChildPlaylistItemBinding childPlaylistItemBinding
                = McChildPlaylistItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(childPlaylistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull McPlayerListAdapter.ViewHolder holder, int position) {

        binderHelper.bind(holder.binding.swipeLayout, String.valueOf(position));

        if (lList.get(position).isPlayingInd())
            holder.binding.imgIsPlaying.setVisibility(View.VISIBLE);
        else
            holder.binding.imgIsPlaying.setVisibility(View.INVISIBLE);

        holder.binding.tvPointsSort.setText(String.valueOf(lList.get(position).getTotalPoints()));
        holder.binding.tvPlayerName.setText(lList.get(position).getPlayerShortName());
        holder.binding.tvPlayerTeamName.setText(lList.get(position).getTeamShortName());

        String text = lList.get(position).getPlayerValue() + "k";
        holder.binding.tvPriceSort.setText(text);

        if (lList.get(position).isPlayerSelected()) {
            holder.binding.clMain.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorGrey));
            holder.binding.swipeLayout.setLockDrag(false);
        } else {
            holder.binding.swipeLayout.setLockDrag(true);
            holder.binding.clMain.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorWhite));
        }

        if (lList.get(position).getPlayerType().equals("overseas"))
            holder.binding.tvPlayerName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_overseas, 0);
        else
            holder.binding.tvPlayerName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        if (lList.get(position).isTeamCapt()) {
            holder.binding.tvCaptain.setVisibility(View.GONE);
            holder.binding.tvVCaptain.setVisibility(View.VISIBLE);
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
            if (prevCapt == -1 || captID==lList.get(position).getPlayerId())
                prevCapt = position;

            captID = lList.get(position).getPlayerId();
        } else if (lList.get(position).isTeamVCapt()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
            holder.binding.tvVCaptain.setVisibility(View.GONE);
            holder.binding.tvCaptain.setVisibility(View.VISIBLE);
            if (prevViceCapt == -1 || viceCaptID==lList.get(position).getPlayerId())
                prevViceCapt = position;

            viceCaptID = lList.get(position).getPlayerId();
        } else {
            holder.binding.imgCVC.setVisibility(View.GONE);
            holder.binding.tvVCaptain.setVisibility(View.VISIBLE);
            holder.binding.tvCaptain.setVisibility(View.VISIBLE);
        }

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

        holder.binding.playerNameBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                itemClickListener.onPlayerClick(lList.get(position));
            }
        });

        holder.binding.clMain.setOnClickListener(view -> {
            if (lList.get(position).isPlayerSelected()) {
                callback.OnRemovePlayer(lList.get(position));
                if (lList.get(position).isTeamCapt()) {
                    captID = 0;
                    prevCapt = -1;
                    lList.get(position).setTeamCapt(false);
                } else if (lList.get(position).isTeamVCapt()) {
                    viceCaptID = 0;
                    prevViceCapt = -1;
                    lList.get(position).setTeamVCapt(false);
                }
            } else {
                if (remainingBudget >= lList.get(position).getPlayerValue()) {
                    int valueST = getSameTeamList(selectedList, lList.get(position).getTeamShortName());
                    if (valueST >= MaxSameTeamPlayer) {
                        String txt = context.getString(R.string.text_maxsameplayer) + " " + MaxSameTeamPlayer;
                        CustomToast.getInstance(context).showSmallCustomToast(txt);
                    } else {
                        int valueOS = getOverSeasList(selectedList, lList.get(position).getPlayerType());
                        if (valueOS >= MaxOverseasPlayer) {
                            String txt = context.getString(R.string.text_maxoverseasplayer) + " " + MaxOverseasPlayer;
                            CustomToast.getInstance(context).showSmallCustomToast(txt);
                        } else {
                            System.out.println("player count adapter " + playerCount);
                            checkPlayerisSelected(lList.get(position));
                        }
                    }
                } else
                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_outofbudget));
            }
        });

        holder.binding.tvCaptain.setOnClickListener(view -> {
            if (!lList.get(position).isPlayerSelected()) {
                if (playerCount < 11)
                    setCaptain(position);
                else
                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_playerselectionlimit));
            } else
                setCaptain(position);

            holder.binding.swipeLayout.close(true);
        });

        holder.binding.tvVCaptain.setOnClickListener(view -> {
            if (!lList.get(position).isPlayerSelected()) {
                if (playerCount < 11)
                    setViceCaptain(position);
                else
                    CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_playerselectionlimit));
            } else
                setViceCaptain(position);

            holder.binding.swipeLayout.close(true);
        });
    }

    public int getTeamCapt(){
        return captID;
    }

    public int getTeamVCapt(){
        return viceCaptID;
    }

    private void setCaptain(int position) {
        if (lList.get(position).isTeamVCapt()) {
            lList.get(position).setTeamVCapt(false);
            prevViceCapt = -1;
            viceCaptID = 0;
        }
        if (prevCapt == -1) {
            lList.get(position).setTeamCapt(true);
            prevCapt = position;
            captID = lList.get(position).getPlayerId();
            checkPlayerisSelected(lList.get(position));
        } else {
            lList.get(position).setTeamCapt(true);
            lList.get(prevCapt).setTeamCapt(false);
            prevCapt = position;
            captID = lList.get(position).getPlayerId();
            checkPlayerisSelected(lList.get(position));
        }
        callback.captainSelect(lList.get(position));
    }

    private void setViceCaptain(int position) {
        if (lList.get(position).isTeamCapt()) {
            lList.get(position).setTeamCapt(false);
            prevCapt = -1;
            captID = 0;
        }
        if (prevViceCapt == -1) {
            lList.get(position).setTeamVCapt(true);
            prevViceCapt = position;
            viceCaptID = lList.get(position).getPlayerId();
            checkPlayerisSelected(lList.get(position));

        } else {
            lList.get(position).setTeamVCapt(true);
            lList.get(prevViceCapt).setTeamVCapt(false);
            prevViceCapt = position;
            viceCaptID = lList.get(position).getPlayerId();
            checkPlayerisSelected(lList.get(position));
        }
        callback.viceCaptainSelect(lList.get(position));
    }

    private void checkPlayerisSelected(PlayerListModel data) {
        if (!data.isPlayerSelected()) {
            if (playerCount < 11) {
                switch (data.getPlayerSpeciality()) {
                    case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                        if (wicketKeeperCount < MaxWicketKeeper)
                            setPlayerAsSelected(data);
                        else
                            CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_wicketkeeperlimitexceed));
                        break;

                    case Constants.TAG_PLAYERTYPE_BATSMAN:
                        if (batsmanCount < MaxBatsman)
                            setPlayerAsSelected(data);
                        else
                            CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_batsmanlimitexceed));
                        break;

                    case Constants.TAG_PLAYERTYPE_BLOWER:
                        if (bolwerCount < MaxBowler)
                            setPlayerAsSelected(data);
                        else
                            CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_bowlerlimitexceed));
                        break;

                    case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                        if (allrunderCount < MaxAllrounder)
                            setPlayerAsSelected(data);
                        else
                            CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_allrounderlimitexceed));
                        break;
                }
            } else
                CustomToast.getInstance(context).showSmallCustomToast(context.getString(R.string.text_playerselectionlimit));
        } else
            notifyDataSetChanged();
    }

    public void updateSelectedPlayer(List<PlayerListModel> list) {
        this.selectedList = list;
    }

    public void updateBudget(int value) {
        this.remainingBudget = value;
    }

    private void setPlayerAsSelected(PlayerListModel playerDataType) {
        playerDataType.setPlayerSelected(true);
        callback.OnSelectPlayer(playerDataType);
        notifyItemRangeInserted(lList.indexOf(playerDataType), lList.size());
    }

    public void refreshBudget(int value) {
        this.remainingBudget = value;
    }

    public void sortData(LinkedList<PlayerListModel> pList) {
        this.lList = pList;
        dictionaryWords = lList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, lList.size());
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void refreshCounts(int batsmanCount, int bolwerCount, int allrunderCount, int wicketKeeperCount, int playerCount) {
        this.wicketKeeperCount = wicketKeeperCount;
        this.bolwerCount = bolwerCount;
        this.batsmanCount = batsmanCount;
        this.allrunderCount = allrunderCount;
        this.playerCount = playerCount;
        notifyDataSetChanged();
    }

    public void updateLimit(int wicketKeeper, int maxBatsman, int maxBowler, int maxAllrounder, int maxSameTeamPlayer, int maxOverseasPlayer) {
        this.MaxWicketKeeper = wicketKeeper;
        this.MaxBatsman = maxBatsman;
        this.MaxBowler = maxBowler;
        this.MaxAllrounder = maxAllrounder;
        this.MaxSameTeamPlayer = maxSameTeamPlayer;
        this.MaxOverseasPlayer = maxOverseasPlayer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private McChildPlaylistItemBinding binding;

        public ViewHolder(@NonNull McChildPlaylistItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public void removeSelectedPlayer(PlayerListModel data, int count) {
        this.playerCount = count;
        lList.get(lList.indexOf(data)).setPlayerSelected(false);
        notifyDataSetChanged();
    }

    public interface IPoints {
        void OnSelectPlayer(PlayerListModel playerDataType);
        void OnRemovePlayer(PlayerListModel playerDataType);
        void captainSelect(PlayerListModel obj);
        void viceCaptainSelect(PlayerListModel obj);
        void updateOverSeasCount(int val);
        void updateSameTeamCount(int val);
    }

    public class CustomFilter extends Filter {
        private final McPlayerListAdapter mAdapter;

        CustomFilter(McPlayerListAdapter mAdapter) {
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
                for (final PlayerListModel mWords : dictionaryWords) {
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

    private int getSameTeamList(List<PlayerListModel> lList, String teamShortName) {
        if (lList.size() > 0) {
            int value = 0;
            HashMap<String, Integer> mapSameTeam = new HashMap<>();
            for (PlayerListModel data : lList) {
                if (mapSameTeam.size() > 0) {
                    if (mapSameTeam.containsKey(data.getTeamShortName())) {
                        int val = mapSameTeam.get(data.getTeamShortName());
                        val++;
                        mapSameTeam.put(data.getTeamShortName(), val);
                    } else
                        mapSameTeam.put(data.getTeamShortName(), 1);
                } else
                    mapSameTeam.put(data.getTeamShortName(), 1);
            }
            if (mapSameTeam.containsKey(teamShortName))
                value = mapSameTeam.get(teamShortName);

            callback.updateSameTeamCount(value);
            return (value);
        } else
            return 0;
    }

    private int getOverSeasList(List<PlayerListModel> lList, String playerType) {
        if (lList.size() > 0) {
            int val = 0;
            HashMap<String, Integer> mapOverSeas = new HashMap<>();
            for (PlayerListModel data : lList) {
                if (mapOverSeas.size() > 0) {
                    if (mapOverSeas.containsKey(data.getPlayerType())) {
                        int pos = mapOverSeas.get(data.getPlayerType());
                        pos++;
                        mapOverSeas.put(data.getPlayerType(), pos);
                    } else
                        mapOverSeas.put(data.getPlayerType(), 1);
                } else
                    mapOverSeas.put(data.getPlayerType(), 1);
            }

            if (playerType.equals("overseas") && mapOverSeas.get("overseas") != null)
                val = mapOverSeas.get("overseas");

            callback.updateOverSeasCount(val);
            return val;
        } else
            return 0;
    }
}
