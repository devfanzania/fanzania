package com.yorker.fanzania.views.screens.tournament.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildWalletClaimsItemBinding;

import java.util.List;

import static com.yorker.fanzania.widgets.DateUtils.getFullDateFromISO;

public class ClaimListAdapter extends RecyclerView.Adapter<ClaimListAdapter.ViewHolder> {

    private List<WalletClaimResponse> lList;

    public ClaimListAdapter(List<WalletClaimResponse> list) {
        this.lList = list;
    }

    @NonNull
    @Override
    public ClaimListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildWalletClaimsItemBinding binding
                = ChildWalletClaimsItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimListAdapter.ViewHolder holder, int position) {

        if(position %2 == 1)
            holder.binding.llPaymentDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
        else
            holder.binding.llPaymentDetails.setBackgroundColor(Color.parseColor("#40E4E4E6"));

        holder.binding.tvBundle.setText(lList.get(position).getBundle());
        getFullDateFromISO(lList.get(position).getClaimDate(),holder.binding.tvPayoutDate);
        holder.binding.tvAmount.setText(String.valueOf(lList.get(position).getClaimAmount()));

        String[] txt =lList.get(position).getVouchar().split("-");
        holder.binding.tvVoucher.setText(new StringBuilder().append(txt[0]).append("..."));

        if (!lList.get(position).getVouchar().equalsIgnoreCase("cash")){
            holder.binding.tvVoucher.setOnClickListener(v->{
                ClipboardManager clipboard = (ClipboardManager) holder.binding.getRoot().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hoyeche", lList.get(position).getVouchar());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(holder.binding.getRoot().getContext(),holder.binding.getRoot().getContext().getString(R.string.text_copiedtoclipboard),Toast.LENGTH_SHORT).show();
            });
        }else{
            holder.binding.tvVoucher.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildWalletClaimsItemBinding binding;

        public ViewHolder(@NonNull ChildWalletClaimsItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
