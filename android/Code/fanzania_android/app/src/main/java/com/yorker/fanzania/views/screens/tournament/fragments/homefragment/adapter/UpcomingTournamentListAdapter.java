package com.yorker.fanzania.views.screens.tournament.fragments.homefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.databinding.ChildTournamentItemBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UpcomingTournamentModel;

import java.util.LinkedList;

public class UpcomingTournamentListAdapter extends RecyclerView.Adapter<UpcomingTournamentListAdapter.ViewHolder> {

    private Context context;

    private ITournament callback = null;

    private LinkedList<UpcomingTournamentModel> lList;

    public UpcomingTournamentListAdapter(Context context, LinkedList<UpcomingTournamentModel> list, ITournament callback) {
        this.context = context;
        this.lList = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public UpcomingTournamentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildTournamentItemBinding binding = ChildTournamentItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingTournamentListAdapter.ViewHolder holder, int position) {

        holder.binding.tvTournamentName.setText(lList.get(position).getTournamentName());
        holder.binding.rrMainlayout.setOnClickListener(view ->
            callback.onClickTournament(lList.get(position)));
    }

    public void AddData(LinkedList<UpcomingTournamentModel> list) {
        lList.clear();
        lList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildTournamentItemBinding binding;

        public ViewHolder(@NonNull ChildTournamentItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface ITournament {

        void onClickTournament(UpcomingTournamentModel upcomingTournamentModel);
    }
}
