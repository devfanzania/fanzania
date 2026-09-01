package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.PlayerDetailDialogBinding;
import com.yorker.fanzania.databinding.TeamInfoDialogBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PlayerDetailDialog extends AlertDialog.Builder {
    private AlertDialog alertDialog;

    public PlayerDetailDialog(Context context, JSONObject jsonObject) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        PlayerDetailDialogBinding binding = DataBindingUtil.inflate(li, R.layout.player_detail_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        try {
            jsonObject = jsonObject.getJSONObject("data");
            String formText = "Total Points: "+String.valueOf(jsonObject.getString("playerPoints1"))+", "+jsonObject.getString("playerPoints2")+", "+jsonObject.getString("playerPoints3")+", "+jsonObject.getString("playerPoints4")+", "+jsonObject.getString("playerPoints5")+"\n";
            String runsText = "Runs Scored : "+String.valueOf(jsonObject.getString("playerRuns1"))+", "+jsonObject.getString("playerRuns2")+", "+jsonObject.getString("playerRuns3")+", "+jsonObject.getString("playerRuns4")+", "+jsonObject.getString("playerRuns5")+"\n";
            String wicketsText = "Wickets Taken : "+String.valueOf(jsonObject.getString("playerWickets1"))+", "+jsonObject.getString("playerWickets2")+", "+jsonObject.getString("playerWickets3")+", "+jsonObject.getString("playerWickets4")+", "+jsonObject.getString("playerWickets5")+"\n";

            binding.tvMyMatch.setText(jsonObject.getString("playerName"));
            binding.selectedBy.setText(jsonObject.getInt("selectedBy")+"% Teams");
            binding.overallRank.setText(String.valueOf(jsonObject.getInt("playerRank")));
            binding.points.setText(String.valueOf(jsonObject.getInt("playerTotalPoints")));
            binding.rank.setText(String.valueOf(jsonObject.getInt("playerValueRank"))+" Out of "+jsonObject.getInt("totalPlayers")+" Players");
            binding.form.setText(formText+runsText+wicketsText);
//            Picasso.with(context).load(jsonObject.getString("imageURL")).into(binding.imgLogo);

            String url = jsonObject.getString("imageURL");
            if(!url.contains("https")){
                url = url.replace("http","https");
            }
//            String url = "http://cricapi.com/playerpic/272262.jpg";
//            final Target mTarget = new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
//                    Log.d("DEBUG", "onBitmapLoaded");
//                    BitmapDrawable mBitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
//                    binding.imgLogo.setImageDrawable(mBitmapDrawable);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable drawable) {
//                    Log.d("DEBUG", "onBitmapFailed");
//                    binding.imgLogo.setImageDrawable(drawable);
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable drawable) {
//                    Log.d("DEBUG", "onPrepareLoad");
//                }
//            };

            Picasso.get()
                    .load(url)
//                    .load("https://www.cricapi.com/playerpic/328026.jpg")
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.imgLogo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        String text = context.getString(R.string.text_infotext1) + " " + minBatsman + "-" + maxBatsman + " " + context.getString(R.string.text_batsman)
//                + " " + minBowler + "-" + maxBowler + " " + context.getString(R.string.text_bowler)
//                + " " + minAllrounder + "-" + maxAllrounder + " " + context.getString(R.string.text_allrounder)
//                + " " + minWicketKeeper+"-"+maxWicketKeeper + " " + context.getString(R.string.text_textinfo2)
//                + " " + maxSameTeamPlayer + " " + context.getString(R.string.text_info3)
//                + " " + maxOverseasPlayer + " " + context.getString(R.string.text_info4);

//        binding.tvDetails.setText(text);
//
//        String txt=context.getString(R.string.text_nitro)+" = "+context.getString(R.string.text_doubleyourpoints)+"\n"+
//                context.getString(R.string.text_autocaptain)+" = "+context.getString(R.string.text_yourhighestscorer)+"\n"+
//                context.getString(R.string.text_painkiller)+" = "+context.getString(R.string.text_yougetmatchtop);
//
//        binding.tvPowerplayDetails.setText(txt);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

//        binding.btnYes.setOnClickListener(view ->
//            alertDialog.dismiss());
    }
}
