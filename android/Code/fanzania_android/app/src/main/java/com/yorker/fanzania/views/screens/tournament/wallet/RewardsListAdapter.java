package com.yorker.fanzania.views.screens.tournament.wallet;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.databinding.ChildWalletRewardsItemBinding;

import java.util.List;

import static com.yorker.fanzania.widgets.DateUtils.getFullDateFromISO;

public class RewardsListAdapter extends RecyclerView.Adapter<RewardsListAdapter.ViewHolder> {

    private List<WalletRewardsResponse> lList;

    public RewardsListAdapter(List<WalletRewardsResponse> list) {
        this.lList = list;
    }

    @NonNull
    @Override
    public RewardsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildWalletRewardsItemBinding binding
                = ChildWalletRewardsItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsListAdapter.ViewHolder holder, int position) {

        if(position %2 == 1)
            holder.binding.llPaymentDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
        else
            holder.binding.llPaymentDetails.setBackgroundColor(Color.parseColor("#40E4E4E6"));

        getFullDateFromISO(lList.get(position).getRewardDate(),holder.binding.tvRewardDate);

        holder.binding.tvRewardType.setText(lList.get(position).getRewardType());
        holder.binding.tvRewardDetails.setText(lList.get(position).getDetails());
        holder.binding.tvRewardsAmount.setText("INR "+String.valueOf(lList.get(position).getRewardAmount()));
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildWalletRewardsItemBinding binding;

        public ViewHolder(@NonNull ChildWalletRewardsItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
