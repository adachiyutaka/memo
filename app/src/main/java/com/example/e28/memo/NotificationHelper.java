package com.example.e28.memo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

/**
 * Created by User on 2019/10/16.
 */

public class NotificationHelper extends ContextWrapper {

//    private static final String CHANNEL_GENERAL_ID = "general";
//    private NotificationManager manager;
//
    public NotificationHelper(Context base) {
        super(base);
//        if (isOreoOrLater()) {
//            NotificationChannel channel = new NotificationChannel(CHANNEL_GENERAL_ID, "General Notifications", NotificationManager.IMPORTANCE_LOW);
//            getManager().createNotificationChannel(channel);
//        }
   }
//
//    public Notification.Builder getNotification() {
//        Notification.Builder builder = isOreoOrLater()
//                ? new Notification.Builder(this, CHANNEL_GENERAL_ID)
//                : new Notification.Builder(this);
//
//        return builder.setContentTitle(getString(R.string.app_name))
//                .setContentText("Hello World!")
//                .setSmallIcon(R.mipmap.ic_launcher);
//    }
//
//    public void notify(int id, Notification.Builder builder) {
//        getManager().notify(id, builder.build());
//    }
//
//    private NotificationManager getManager() {
//        if (manager == null) {
//            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        return manager;
//    }
//
//    private boolean isOreoOrLater() {
//        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
//    }
}

