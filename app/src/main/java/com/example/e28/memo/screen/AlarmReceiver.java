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
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Repeat;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 2019/10/16.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Alarmから送られたIntentの内容を受け取る
        Bundle bundle = intent.getExtras();
        long memoId = bundle.getLong("MEMO_ID");
        int todoListId = bundle.getInt("TODOLIST_ID");

        Realm realm = Realm.getDefaultInstance();
        Memo memo = realm.where(Memo.class).equalTo("id", memoId).findFirst();
        Repeat repeat = realm.where(Repeat.class).equalTo("id", memo.getTodoList().get(todoListId).getRepeatId()).findFirst();
        //logd で前後のremindCountを読み取り
        // リマインダーの残り回数を1回減らす
        repeat.setNotifyRemainCount(repeat.getNotifyRemainCount() - 1);
        //logd

        // RepeatをRealmに保存する
 //       MyRealm.saveReam(repeat);
        // Realmのラッパークラスを作る

        //新しいアラームを設定する
        AlarmCreator alarmCreator = new AlarmCreator();
        alarmCreator.createAlarm(memoId, todoListId);

        // 通知カードをタップした際に、アプリへ移動するためのIntent
        int requestCode = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 通知チャンネルの作成
        String channelId = "reminder";
        // チャンネルのタイトルにapp_nameを指定
        String title = context.getString(R.string.notification_channel_name);

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);
        String cTime = dataFormat.format(currentTime);

        // メッセージ　+ 11:22:331
        String message = memo.getText();

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
//                        .setSubText(content)
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