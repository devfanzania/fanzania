package com.yorker.fanzania.customviews;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yorker.fanzania.R;

public class CustomToast {

    private Context mContext;
    private static CustomToast instance;

    private CustomToast(Context context) {
        this.mContext = context;
    }

    public synchronized static CustomToast getInstance(Context context) {
        if (instance == null) {
            instance = new CustomToast(context);
        }
        return instance;
    }

    public void showLongCustomToast(String message) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) ((Activity) mContext).findViewById(R.id.toast_layout_root));
        TextView msgTv = (TextView) layout.findViewById(R.id.text);
        msgTv.setText(message);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    public void showSmallCustomToast(String message) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) ((Activity) mContext).findViewById(R.id.toast_layout_root));
        TextView msgTv = (TextView) layout.findViewById(R.id.text);
        msgTv.setText(message);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


}
