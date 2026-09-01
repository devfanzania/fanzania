package com.yorker.fanzania.views.screens.tournament.playerlist.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ChildSelectedPlaylistItemBinding;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class SelectedPlayerListAdapter extends RecyclerView.Adapter<SelectedPlayerListAdapter.ViewHolder> {

    private ISelectedPlayer callback = null;
    private LinkedList<PlayerDataType> lList;
    private Context mContext;

    public SelectedPlayerListAdapter(Context context, LinkedList<PlayerDataType> list, ISelectedPlayer callback) {
        mContext = context;
        this.lList = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public SelectedPlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ChildSelectedPlaylistItemBinding binding
                = ChildSelectedPlaylistItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPlayerListAdapter.ViewHolder holder, int position) {

        if (lList.get(position).isPlayingInd())
            holder.binding.tvPlayerName.setText(Html.fromHtml("<font color='#7DBD21'>" + lList.get(position).getPlayerShortName() + "</font><font color='#2D415A'>"
                    +" "+ lList.get(position).getPlayerValue() + "K" + "</font>"));
        else
            holder.binding.tvPlayerName.setText(Html.fromHtml("<font color='#2D415A'>" + lList.get(position).getPlayerShortName() + "</font><font color='#2D415A'>"
                    +" "+ lList.get(position).getPlayerValue() + "K" + "</font>"));

        if (lList.get(position).getTeamCapt() == lList.get(position).getPlayerId()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_c);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
        } else if (lList.get(position).getTeamVCapt() == lList.get(position).getPlayerId()) {
            holder.binding.imgCVC.setImageResource(R.drawable.ic_vc);
            holder.binding.imgCVC.setVisibility(View.VISIBLE);
        } else {
            holder.binding.imgCVC.setVisibility(View.GONE);
        }

        holder.binding.tvTeamName.setText(lList.get(position).getTeamShortName());

        if (lList.get(position).getPlayerType().equals("overseas"))
            holder.binding.tvTeamName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_overseas, 0, 0, 0);
        else
            holder.binding.tvTeamName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        switch (lList.get(position).getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                holder.binding.imgLogo.setImageResource(R.drawable.ic_new_batsman);
                break;
            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                holder.binding.imgLogo.setImageResource(R.drawable.ic_new_allrounder);
                break;
            case Constants.TAG_PLAYERTYPE_BLOWER:
                holder.binding.imgLogo.setImageResource(R.drawable.ic_new_bowler);
                break;
            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                holder.binding.imgLogo.setImageResource(R.drawable.ic_new_keeper);
                break;
        }

        holder.binding.imgRemove.setOnClickListener(view -> {
            callback.OnRemovePlayer(lList.get(position));
            lList.remove(lList.indexOf(lList.get(position)));
            notifyDataSetChanged();
        });

        holder.binding.tvPlayerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.OnSelectPlayerNew(lList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public void addNewPlayer(PlayerDataType playerDataType) {
        lList.add(playerDataType);
        notifyDataSetChanged();
    }

    public LinkedList<PlayerDataType> getList() {
        return lList;
    }

    public int getSameTeamList() {
        HashMap<String, Integer> mapSameTeam = new HashMap<>();
        for (PlayerDataType data : lList) {
            if (mapSameTeam.size() > 0) {
                if (mapSameTeam.containsKey(data.getParticipationTeamName())) {
                    int val = mapSameTeam.get(data.getParticipationTeamName());
                    val++;
                    mapSameTeam.put(data.getParticipationTeamName(), val);
                } else
                    mapSameTeam.put(data.getParticipationTeamName(), 1);
            } else
                mapSameTeam.put(data.getParticipationTeamName(), 1);
        }

        return (Collections.max(mapSameTeam.values()));
    }

    public int getOverSeasList() {
        int val = 0;
        HashMap<String, Integer> mapOverSeas = new HashMap<>();
        for (PlayerDataType data : lList) {
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
        if (mapOverSeas.get("overseas") != null)
            val = mapOverSeas.get("overseas");

        return val;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildSelectedPlaylistItemBinding binding;

        public ViewHolder(@NonNull ChildSelectedPlaylistItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ISelectedPlayer {
        void OnRemovePlayer(PlayerDataType playerDataType);
        void OnSelectPlayerNew(PlayerDataType playerDataType);
    }
}
