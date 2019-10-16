package com.example.e28.memo.screen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by User on 2019/10/16.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_GENERAL_ID = "general";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (isOreoOrLater()) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_GENERAL_ID, "General Notifications", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
        String content = intent.getStringExtra("alarm");
        notificationManager.notify((int)SystemClock.uptimeMillis(), buildNotification(context, content));
    }

    private Notification buildNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("うわ～～～～～！！！")
                .setContentText(content)
                .setSmallIcon(android.R.drawable.sym_def_app_icon);

        return builder.build();
    }

    private boolean isOreoOrLater() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    }
}
