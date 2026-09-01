package com.yorker.fanzania.views.screens.notification;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yorker.fanzania.databinding.ChildNotificationBinding;
import com.yorker.fanzania.widgets.DateUtils;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private List<NotificationResponse> lList;
    private INotification callback;

    public NotificationListAdapter(List<NotificationResponse> list,INotification callback) {
        this.lList = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildNotificationBinding binding
                = ChildNotificationBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.ViewHolder holder, int position) {
        holder.binding.tvTitle.setText(lList.get(position).getTitle());
        holder.binding.tvContent.setText(lList.get(position).getMessage());
        DateUtils.getDateFromISO(lList.get(position).getUpdateDate(), holder.binding.tvDate);

        holder.binding.imageView.setOnClickListener(v->{
            callback.deleteNotification(lList.get(position));
            lList.remove(lList.get(position));

            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildNotificationBinding binding;

        public ViewHolder(@NonNull ChildNotificationBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface INotification {

        void deleteNotification(NotificationResponse val);
    }

}
