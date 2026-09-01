package com.yorker.fanzania.views.imagecapture;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

public interface DialogBuilderInterfaceCustom {

    void onInvokeDialog(DialogInterface dia);
    void onDialogClose(DialogInterface dia);
    void getDialogContents(View dialog_view, Dialog dialog_instance);
}

