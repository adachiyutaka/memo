package com.example.e28.memo.screen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Repeat;
import com.example.e28.memo.model.Todo;

import java.util.Calendar;

import io.realm.Realm;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by User on 2019/11/18.
 */

public class AlarmCreator {

    public void alarmCreator () {
        return;
    }


    public void createAlarm(long memoId, int todoListId){

        Realm realm = Realm.getDefaultInstance();
        Memo memo = realm.where(Memo.class).equalTo("id", memoId).findFirst();
        Todo todo = memo.getTodoList().get(todoListId);
        Repeat repeat = realm.where(Repeat.class).equalTo("id", todo.getRepeatId()).findFirst();
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(todo.getNotifyStartTime());

        // 残り通知回数が0の場合は何もしない
        if (repeat.getNotifyRemainCount() == 0) {
            return;
        }

//        // AlarmReceiverにブロードキャストを行うintentを作成
//        Intent intent = new Intent(getActivity, AlarmReceiver.class);
//// 通知内容と通知のリピートを紐付けるために各IDを渡す
//        Bundle bundle = new Bundle();
//        bundle.putLong("MEMO_ID", memoId);
//        bundle.putInt("TODOLIST_ID", todoListId);
//        intent.putExtras(bundle);
//
//        // 指定時間後に動作し、ブロードキャスト用のintentを起動させるPendingIntentを作成
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, intent, 0);
//
//        // アラームをセットする
//        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(getNextRemindTime(startTime, repeat).getTimeInMillis(), null), pendingIntent);
//        }
    }


    // 次の通知時間を作成する
    public Calendar getNextRemindTime(Calendar startTime, Repeat repeat) {

        Calendar remindTime = Calendar.getInstance();
        int remindCount = repeat.getNotifyRemainCount();
        int endCount = repeat.getNotifyEndCount();
        int notifiedCount = endCount - remindCount;
        int repeatInterval = repeat.getRepeatInterval();
        int startDOW = remindTime.get(Calendar.DAY_OF_WEEK); // 通知開始日の曜日

        // 初回の通知開始時間を基準に時間を加算する
        remindTime.setTime(startTime.getTime());


        switch (repeat.getRepeatScale()) {
            case 0:
                break;
            case 1: // 通知間隔が「日」の場合
                remindTime.add(Calendar.DAY_OF_MONTH, notifiedCount * repeatInterval);
                break;
            case 2: // 通知間隔が「週」の場合
                int notifyDOWCount = 0; // 設定された曜日の数
                boolean[] notifyDOWList = new boolean[7]; // 曜日と通知の有無をbooleanで表現した配列

                // notifyDOWCount　と　notifyDOWList　を　repeat から取得する
                for (int i = 0; i < 7; i ++) {
                    switch (i) {
                        case 0 :
                            notifyDOWList[i] = repeat.isNotifySunday();
                            break;
                        case 1 :
                            notifyDOWList[i] = repeat.isNotifyMonday();
                            break;
                        case 2 :
                            notifyDOWList[i] = repeat.isNotifyTuesday();
                            break;
                        case 3 :
                            notifyDOWList[i] = repeat.isNotifyWednesday();
                            break;
                        case 4 :
                            notifyDOWList[i] = repeat.isNotifyThursday();
                            break;
                        case 5 :
                            notifyDOWList[i] =  repeat.isNotifyFriday();
                            break;
                        case 6 :
                            notifyDOWList[i] = repeat.isNotifySaturday();
                            break;
                    }
                    if (notifyDOWList[i] == true) notifyDOWCount ++;
                }

                int notifyDOW; // 次に通知を行う曜日

                // 通知開始日の週に含まれる通知する日の数を取得
                int notifiedCountInStartWeek = getDOWCount(notifyDOWList, startDOW, 6);

                if (notifiedCount < notifiedCountInStartWeek) {
                    // 通知開始日の週に、まだ通知していない日がある場合
                    // 通知開始日の曜日を基準に、通知した回数分曜日を移動させ、通知する曜日を取得
                    notifyDOW = getNextDOW(notifyDOWList, startDOW, notifyDOWCount);
                } else {
                    // 2週目以降の場合
                    int notifiedWeekCount = 1 + (notifiedCount - notifiedCountInStartWeek) / notifyDOWCount; // 週2回通知で7回通知済みの場合、3　になる
                    remindTime.add(Calendar.WEEK_OF_MONTH, notifiedWeekCount * repeatInterval); // 通知する週を設定する

                    notifyDOWCount = (notifiedCount - notifiedCountInStartWeek) % notifyDOWCount; // 週2回通知で7回通知済みの場合、1　になる
                    notifyDOW = getNextDOW(notifyDOWList, 0, notifyDOWCount); // 通知を行う曜日
                }
                remindTime.set(Calendar.DAY_OF_WEEK, notifyDOW); // 通知する曜日を設定する
                break;
            case 3: // 通知間隔が「月」の場合
                remindTime.add(Calendar.DAY_OF_MONTH, notifiedCount * repeatInterval);

                if (repeat.isNotifySameDay()) {
                    // 同じ日の場合
                    remindTime.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH));
                } else if (repeat.isNotifySameDOW()) {
                    //　同じ第N *曜日の場合
                    remindTime.set(Calendar.WEEK_OF_MONTH, startTime.get(Calendar.WEEK_OF_MONTH));
                    remindTime.set(Calendar.DAY_OF_WEEK, startTime.get(Calendar.DAY_OF_WEEK));
                } else if (repeat.isNotifySameLastDay()) {
                    // 末日の場合
                    remindTime.set(Calendar.DAY_OF_MONTH, remindTime.getActualMaximum(Calendar.DATE));
                }
                break;
            case 4: // 通知間隔が「年」の場合
                remindTime.add(Calendar.YEAR, notifiedCount * repeatInterval);
                break;
        }

        return remindTime;
    }


    // 次に通知する曜日を作成する
    public int getNextDOW(boolean[] notifyDOW, int startDOW, int skipCount) {
        int nextDOW = 0;
        for (int i = startDOW; i < notifyDOW.length; i++) {
            if(notifyDOW[i] == true) {
                skipCount --;
                if (skipCount == 0) {
                    nextDOW = i;
                    break;
                }
            }
        }
        return nextDOW;
    }


    // 1回目の通知開始日時の翌日～土曜日の間に含まれる、通知する曜日の個数を返す
    public int getDOWCount(boolean[] notifyDOW, int startDOW, int endDOW) {
        // リマインダーを設定した当日に通知されるため、通知した数は最低でも「1」となる
        int DOWCount = 1;

        // 1回目の通知開始日時の曜日が土曜日（startDOW = 6）の場合、1を返す
        if (startDOW == 6) {
            return DOWCount;
        }

        // 1回目の通知開始日時の翌日～土曜日の間に含まれる、通知する曜日の個数
        for (int i = startDOW + 1; i <= endDOW; i++) {
            if (notifyDOW[i] == true) {
                DOWCount ++;
            }
        }
        return DOWCount;
    }
}