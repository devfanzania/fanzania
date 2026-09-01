package com.yorker.fanzania.views.imagecapture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;

public class CMDialogBuilder implements DialogBuilderInterfaceCustom
{
    int color_back = R.color.colorAccent;
    private String message = "";
    private String button_text_positive = "";
    private String button_text_negetive = "";
    private Context mcontext;
    private AlertDialog.Builder dia_help;
    private Dialog dialog_custom;
    private LayoutInflater inflter;
    private View dialog_view;
    private int dialog_layout_resource;

    public CMDialogBuilder(Context context) {
        mcontext=context;
    }

    public CMDialogBuilder(Context context, String positive_button_text, String dialog_message) {
        mcontext = context;
        button_text_positive = positive_button_text;
        message = dialog_message;
        makeDialogBuilderWithOneAction();
    }

    public CMDialogBuilder(int layout_resource, Context context) {
        mcontext = context;
        dialog_layout_resource = layout_resource;
        inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog_view = inflter.inflate(dialog_layout_resource, null);
        getDialogContents(dialog_view, dialog_custom);
    }

    public CMDialogBuilder(Context context, String positive_button_text, String negetive_button_text, String dialog_message) {
        mcontext = context;
        button_text_positive = positive_button_text;
        button_text_negetive = negetive_button_text;
        message = dialog_message;
        makeDialogBuilderWithTwoAction();
    }


    private void makeDialogBuilderWithTwoAction() {
        try {
            dia_help = new AlertDialog.Builder(mcontext);
            dia_help.setMessage(message);
            dia_help.setCancelable(false);
            dia_help.setPositiveButton(button_text_positive, (dialog, which) -> onInvokeDialog(dialog)).setNegativeButton(button_text_negetive, (dialog, which) -> onDialogClose(dialog)).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void makeDialogBuilderWithOneAction() {
        try {
            dia_help = new AlertDialog.Builder(mcontext);
            dia_help.setMessage(message);
            dia_help.setCancelable(false);
            dia_help.setPositiveButton(button_text_positive, (dialog, which) -> onInvokeDialog(dialog)).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onInvokeDialog(DialogInterface dia) {

    }

    @Override
    public void onDialogClose(DialogInterface dia) {

    }

    @Override
    public void getDialogContents(View dialog_view, Dialog dialog_instance) {
        try {
            dialog_custom = new Dialog(mcontext);
            dialog_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog_custom.setContentView(dialog_view);
            dialog_custom.setCanceledOnTouchOutside(false);
            dialog_custom.show();
            getDialogContents(dialog_view, dialog_custom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class showCameraOptions implements CameraInterfaceDia {
        @Override
        public void onCameraInvoked() {

        }

        public showCameraOptions(final Context cotext){
           try {
               final AlertDialog.Builder dia_help = new AlertDialog.Builder(cotext);
               dia_help.setMessage(cotext.getString(R.string.Pleasechoosefollowingoptionforselectimage));
               dia_help.setCancelable(false);

               dia_help.setPositiveButton(cotext.getString(R.string.Camera), (dialog, which) -> {
                   dialog.dismiss();
                   onCameraInvoked();
                   Constants.CAMERA = true;
                   Intent intent = new Intent(cotext, ActivityImageTaker.class);
                   ((AppCompatActivity) cotext).startActivityForResult(intent, Constants.ACTION_TAKE_CAMERA);

               }).setNegativeButton(cotext.getString(R.string.Gallery), (dialog, which) -> {
                   dialog.dismiss();
                   onCameraInvoked();
                   try {
                       Intent intent = new Intent();
                       intent.setType("image/*");
                       intent.setAction(Intent.ACTION_GET_CONTENT);
                       ((AppCompatActivity) cotext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.ACTION_TAKE_GALLERY);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }).setNeutralButton(cotext.getString(R.string.text_cancel),
                       (dialog, id) -> dialog.dismiss()).show();
           } catch (Exception e) {
               e.printStackTrace();
           }
        }
    }
}
