package com.example.e28.memo.screen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.example.e28.memo.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 2019/10/16.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO: 2019/10/17 ↓これ何？よくわかってない
        Log.d("AlarmBroadcastReceiver", "onReceive() pid=" + android.os.Process.myPid());

        long memoId = intent.getLongExtra("RequestCode", -1);

        //
        //requestCode 実際はMemoのIDにするべきところ
        //
        int requestCode = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "reminder";
        // app name
        String title = context.getString(R.string.notification_channel_name);

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);
        String cTime = dataFormat.format(currentTime);

        // メッセージ　+ 11:22:331
        String message = "時間になりました。" + cTime;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // デフォルトで設定されている通知音を指定
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // API26以上の場合のみ記述している
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // NotificationChannel 設定
            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(context.getString(R.string.notification_channel_discription)); //通知チャンネルの説明
            channel.enableVibration(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            // the channel appears on the lockscreen
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setSound(defaultSoundUri, null);
            channel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(channel);

                Notification notification = new Notification.Builder(context, channelId)
                        .setContentTitle(title)
                        // android標準アイコンから
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentText(message)
                        .setSubText(String.valueOf(memoId))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();

                // 通知
                notificationManager.notify(R.string.app_name, notification);
            }
        }
    }
}
