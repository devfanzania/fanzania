package com.yorker.fanzania.views.screens.tournament.pointsbreakdown.adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildPointBreakItemBinding;

import java.util.LinkedList;

public class MyPointBreakDownListAdapter extends RecyclerView.Adapter<MyPointBreakDownListAdapter.ViewHolder> {

    private Context context;

    private IPoints callback = null;

    private LinkedList<String> lList;

    public MyPointBreakDownListAdapter(Context context, LinkedList<String> list, IPoints callback) {
        this.context = context;
        this.lList = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyPointBreakDownListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildPointBreakItemBinding childPointBreakItemBinding
                = ChildPointBreakItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(childPointBreakItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPointBreakDownListAdapter.ViewHolder holder, int position) {
        if (position % 2 == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                holder.binding.rrHeader.setBackgroundColor(context.getResources().getColor(R.color.colorLightYellow, context.getTheme()));
            else
                holder.binding.rrHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightYellow));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                holder.binding.rrHeader.setBackgroundColor(context.getResources().getColor(R.color.colorLightGrey, context.getTheme()));
            else
                holder.binding.rrHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGrey));
        }
    }

    @Override
    public int getItemCount() {
        return 18;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildPointBreakItemBinding binding;

        public ViewHolder(@NonNull ChildPointBreakItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface IPoints {

    }
}
