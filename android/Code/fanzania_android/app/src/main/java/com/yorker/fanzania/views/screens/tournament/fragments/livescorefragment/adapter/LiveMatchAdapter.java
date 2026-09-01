package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ChildItemTabLiveMatchBinding;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LiveMatchModel;
import java.util.LinkedList;

public class LiveMatchAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private LinkedList<LiveMatchModel> list;
    private Context mContext;

    public LiveMatchAdapter(Context context, LinkedList<LiveMatchModel> lk_tournaments) {
        mContext = context;
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
        ChildItemTabLiveMatchBinding sbinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.child_item_tab_live_match,
                null, false);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.blank_jersey);
        requestOptions.error(R.drawable.blank_jersey);

        sbinding.tvLeftTeamName.setText(list.get(position).getTeam1ShortName());

        String url1 = Constants.BASE_IMAGE_URL + list.get(position).getTeam1Image();
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(url1).into(sbinding.imgLeftTeamLogo);

        sbinding.tvRightTeamName.setText(list.get(position).getTeam2ShortName());

        String url2 = Constants.BASE_IMAGE_URL + list.get(position).getTeam2Image();
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(url2).into(sbinding.imgRightTeamLogo);

        container.addView(sbinding.getRoot());

        return sbinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
