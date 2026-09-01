package com.yorker.fanzania.views.screens.matchcontest.fragments.home;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildTeamListItemBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.TournamentModel;

import java.util.List;

public class TournamentFilterListAdapter extends RecyclerView.Adapter<TournamentFilterListAdapter.ViewHolder> {

    private List<TournamentModel> lList;
    private ITeamList iTeamList;

    public TournamentFilterListAdapter(List<TournamentModel> list, ITeamList iTeamList) {
        this.lList = list;
        this.iTeamList = iTeamList;
    }

    @NonNull
    @Override
    public TournamentFilterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildTeamListItemBinding binding
                = ChildTeamListItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentFilterListAdapter.ViewHolder holder, int position) {

        holder.binding.tvTeamName.setText(lList.get(position).getTournamentName());

        if (lList.get(position).isChecked())
            holder.bgBackground.setColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorDark));
        else
            holder.bgBackground.setColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorGrey));

        holder.binding.rrHeader.setOnClickListener(view -> {
            checkFunction(position, holder);
        });
    }

    private void checkFunction(int position, ViewHolder holder) {
        if (lList.get(position).isChecked()) {
            lList.get(position).setChecked(false);
            holder.bgBackground.setColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorGrey));
            iTeamList.OnRemoveTeam(lList.get(position).getTournamentName());
        } else {
            lList.get(position).setChecked(true);
            holder.bgBackground.setColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.colorDark));
            iTeamList.OnSelectTeam(lList.get(position).getTournamentName());
        }
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildTeamListItemBinding binding;
        private GradientDrawable bgBackground;

        public ViewHolder(@NonNull ChildTeamListItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            bgBackground = (GradientDrawable) binding.tvTeamName.getBackground();
        }
    }

    public interface ITeamList {
        void OnSelectTeam(String teamShortName);

        void OnRemoveTeam(String teamShortName);
    }
}
