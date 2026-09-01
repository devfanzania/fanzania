package com.yorker.fanzania.views.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;

public class GetKeyHash {

    private Context context;

    public GetKeyHash(Context context) {
        this.context = context;
    }

    @SuppressLint("PackageManagerGetSignatures")
    public String printKeyHash() {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
            }
        } catch (Exception ignored) {
        }
        return key;
    }
}
