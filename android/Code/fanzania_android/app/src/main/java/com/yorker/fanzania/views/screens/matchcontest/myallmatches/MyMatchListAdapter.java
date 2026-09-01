package com.yorker.fanzania.views.screens.matchcontest.myallmatches;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ChildMyMatchListBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.yorker.fanzania.widgets.DateUtils.getFullDateFromISO;
import static com.yorker.fanzania.widgets.DateUtils.printDifference;

public class MyMatchListAdapter extends RecyclerView.Adapter<MyMatchListAdapter.ViewHolder> {

    private List<DailyMatchModel> lList;
    private itemCallBack iCallBack;

    public MyMatchListAdapter(List<DailyMatchModel> list, itemCallBack callBack) {
        this.lList = list;
        this.iCallBack = callBack;
    }

    @NonNull
    @Override
    public MyMatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChildMyMatchListBinding binding
                = ChildMyMatchListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMatchListAdapter.ViewHolder holder, int position) {
        holder.binding.tvTrournamentName.setText(lList.get(position).getTournamentName());
        holder.binding.tvLeftTeamName.setText(lList.get(position).getTeam1ShortName());
        holder.binding.tvRightTeamName.setText(lList.get(position).getTeam2ShortName());
        holder.binding.tvTournamentStatus.setText(lList.get(position).getMatchStatus());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.blank_jersey);
        requestOptions.error(R.drawable.blank_jersey);

        if (lList.get(position).getTeam1Image() != null) {
            String url = Constants.BASE_IMAGE_URL + lList.get(position).getTeam1Image();
            Glide.with(holder.binding.getRoot().getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(holder.binding.imgLeft);
        } else
            holder.binding.imgLeft.setImageResource(R.drawable.blank_jersey);

        if (lList.get(position).getTeam2Image() != null) {
            String url = Constants.BASE_IMAGE_URL + lList.get(position).getTeam2Image();
            Glide.with(holder.binding.getRoot().getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(holder.binding.imgRight);
        } else
            holder.binding.imgRight.setImageResource(R.drawable.blank_jersey);

        if (lList.get(position).getMatchStatus().equals("Live"))
            holder.binding.tvTournamentStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_round,0,0,0);
        else
            holder.binding.tvTournamentStatus.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

        if (lList.get(position).getMatchStatus().equals("UPCOMING"))
        {
            try {
                SimpleDateFormat normalDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                normalDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date2 = normalDateFormat.parse(lList.get(position).getMatchDate());
                Date date1 = normalDateFormat.parse(normalDateFormat.format(Calendar.getInstance().getTime()));
                printDifference(date1, date2,holder.binding.tvTournamentTxt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else
            getFullDateFromISO(lList.get(position).getMatchDate(),holder.binding.tvTournamentTxt);

        if (lList.get(position).getMatchStatus().equals("COMPLETE")){
            String txt=holder.binding.getRoot().getContext().getString(R.string.text_Yougot)+" "+lList.get(position).getTotalPoints()+" pts";
            holder.binding.tvTournamentPts.setText(txt);
        }

        holder.binding.llMain.setOnClickListener(v->{
            iCallBack.onItemClick(lList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return lList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChildMyMatchListBinding binding;

        public ViewHolder(@NonNull ChildMyMatchListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface itemCallBack{
        void onItemClick(DailyMatchModel obj);
    }

}
