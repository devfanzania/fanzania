package com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.yorker.fanzania.views.screens.auth.registration.model.CountryListModel;
import com.yorker.fanzania.views.screens.tournament.playerlist.model.TeamFilterModel;

import java.util.LinkedList;

public class CustomTeamSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private LinkedList<TeamFilterModel> asr;

    public CustomTeamSpinnerAdapter(Context context, LinkedList<TeamFilterModel> asr) {
        this.asr = asr;
        activity = context;
    }

    public int getCount() {
        return asr.size();
    }

    public Object getItem(int i) {
        return asr.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);

        txt.setPadding(10, 10, 10, 10);
        txt.setTextSize(14);
        txt.setGravity(Gravity.CENTER);
        txt.setText(asr.get(position).getTeamShortName());
        txt.setTextColor(Color.parseColor("#5B5B5B"));
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(12, 8, 16, 8);
        txt.setTextSize(14);
        txt.setCompoundDrawablePadding(0);
//        txt.setText(asr.get(i).getCountry());
        txt.setTextColor(Color.parseColor("#5B5B5B"));
        return txt;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return true;
        } else {
            return true;
        }
    }
}
