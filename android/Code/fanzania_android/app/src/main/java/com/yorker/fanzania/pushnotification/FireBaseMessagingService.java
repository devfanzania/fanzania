package com.yorker.fanzania.pushnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yorker.fanzania.R;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;

public class FireBaseMessagingService
        extends FirebaseMessagingService {
    private static int NOTIFICATION_ID = 1;
    private String message;

    private Boolean isbackgrnd;
    private Intent i;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        shownoti(remoteMessage.getData().toString(), remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        System.out.println("device token " + s);
        if (s != null)
            storeToken(s);
    }


    private void storeToken(String token) {
        //saving the token on shared preferences
        System.out.println("device token " + token);
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceTokenno(token);
    }

    private void shownoti(String msg, RemoteMessage remoteMessage) {
        System.out.println("msg=" + msg);
        System.out.println("remoteMessage=" + remoteMessage.getData());
    }

    private void ShowNotification(RemoteMessage remoteMessage) {
        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        message = remoteMessage.getData().get("description");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_appicon);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_appicon)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentTitle(getString(R.string.fanzanianotification))
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setDefaults(-1);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(++NOTIFICATION_ID, mBuilder.build());
    }

//
//    public static Activity getRunningActivity()
//    {
//        try {
//            Class activityThreadClass = Class.forName("android.app.ActivityThread");
//            Object activityThread = activityThreadClass.getMethod("currentActivityThread")
//                    .invoke(null);
//            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
//            activitiesField.setAccessible(true);
//            ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
//            for (Object activityRecord : activities.values()) {
//                Class activityRecordClass = activityRecord.getClass();
//                Field pausedField = activityRecordClass.getDeclaredField("paused");
//                pausedField.setAccessible(true);
//                if (!pausedField.getBoolean(activityRecord)) {
//                    Field activityField = activityRecordClass.getDeclaredField("activity");
//                    activityField.setAccessible(true);
//                    return (Activity) activityField.get(activityRecord);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        throw new RuntimeException("Didn't find the running activity");
//    }
//
//    private String GetCurrentActivityName()
//    {
//        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        return cn.getClassName().toString();
//    }
//
//
//    private boolean isAppIsInBackground(Context context) {
//        boolean isInBackground = true;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    for (String activeProcess : processInfo.pkgList) {
//                        if (activeProcess.equals(context.getPackageName())) {
//                            isInBackground = false;
//                        }
//                    }
//                }
//            }
//        } else {
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//            ComponentName componentInfo = taskInfo.get(0).topActivity;
//            if (componentInfo.getPackageName().equals(context.getPackageName())) {
//                isInBackground = false;
//            }
//        }
//
//        return isInBackground;
//    }
}