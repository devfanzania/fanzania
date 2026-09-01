package com.yorker.fanzania.helper;

import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.ContentHandler;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static java.security.AccessController.getContext;

public class ClearGlideCacheAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private boolean result;
    private Context context;

    public ClearGlideCacheAsyncTask(Context context){
        this.context = context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Glide.get(context).clearDiskCache();
            result = true;
        }
        catch (Exception e){
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
//        if(result)
//            Toast.makeText(context, "cache deleted", Toast.LENGTH_SHORT).show();
    }
}