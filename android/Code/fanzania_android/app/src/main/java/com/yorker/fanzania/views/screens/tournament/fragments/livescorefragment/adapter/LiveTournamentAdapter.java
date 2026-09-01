package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChildItemTabNewBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LiveTournamentModel;

import java.util.LinkedList;

public class LiveTournamentAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private LinkedList<LiveTournamentModel> list;

    public LiveTournamentAdapter(Context context, LinkedList<LiveTournamentModel> lk_tournaments) {
        Context mContext = context;
        list = lk_tournaments;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ChildItemTabNewBinding sbinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.child_item_tab_new,
                null, false);

        sbinding.tvTrounamentName.setText(list.get(position).getTournamentName());
        String txt=list.get(position).getTournamentStartDate()+" - "+list.get(position).getTournamentEndDate();
        sbinding.tvTrounamentYear.setText(txt);

        container.addView(sbinding.getRoot());

        return sbinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
