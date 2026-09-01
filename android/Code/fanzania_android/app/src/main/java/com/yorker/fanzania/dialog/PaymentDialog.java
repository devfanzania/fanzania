package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PaymentDialog {
    public interface PaymentSelectionListener {
        void onPaymentSelected(String paymentGateway);
    }

    public static void showPaymentDialog(Context context, PaymentSelectionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Payment Gateway");
        String[] options = {"Razorpay", "Cashfree"};
        
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPaymentSelected(options[which]);
            }
        });
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
