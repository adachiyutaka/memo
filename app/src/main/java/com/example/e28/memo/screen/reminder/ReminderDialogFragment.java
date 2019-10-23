package com.example.e28.memo.screen.reminder;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Repeat;
import com.example.e28.memo.model.Todo;
import com.example.e28.memo.screen.AlarmReceiver;
import com.example.e28.memo.screen.WriteActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 2019/10/14.
 */

public class ReminderDialogFragment extends DialogFragment {

    Context context;
    Realm realm;
    Todo todo;
    long todoId;
    // Repeat repeat;
    Calendar now;
    Calendar uneditedRemindTime;
    Calendar remindTime;
    private ReminderDialogFragmentListener listener;
    private AlarmManager am;
    private PendingIntent pending;
    private int requestCode = 1;
    Repeat repeat;
    ReminderSpinnerAdapter dateAdapter;
    ReminderSpinnerAdapter timeAdapter;
    ReminderSpinnerAdapter repeatAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        todo = new Todo();

        final String[] prefKeyList = getResources().getStringArray(R.array.pref_key_reminder_array);

        now = Calendar.getInstance();
        final int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);
        final int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        final int hour = now.get(Calendar.HOUR_OF_DAY);
        final int minute = now.get(Calendar.MINUTE);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String[] week = new String[7];
        week[0] = "日";
        week[1] = "月";
        week[2] = "火";
        week[3] = "水";
        week[4] = "木";
        week[5] = "金";
        week[6] = "土";

        int week_int = now.get(Calendar.DAY_OF_WEEK);//曜日を数値で取得

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_reminder);


        // TodoのIDを取得し、セットされた通知開始時間を取得する
        todoId = getArguments().getLong(WriteActivity.TODO_ID);
        remindTime = Calendar.getInstance();
        if (realm.where(Todo.class).equalTo("id", todoId).findFirst() == null) {
            // 新規作成されたTodoだった場合、現在の時間をセットする
            remindTime.setTimeInMillis(now.getTimeInMillis());
        } else {
            // 既存のTodoだった場合、その時間を取得する
            remindTime.setTimeInMillis(realm.where(Todo.class).equalTo("id", todoId).findFirst().getNotifyStartTime());
        }

        // TODO: 2019/10/14 スピナーの表示はカスタムアダプターで設定する必要あり（選択項目と確定項目の表示が違う、選択項目に日付を入れるなど）

        Spinner dateSpinner = dialog.findViewById(R.id.spinner_date);
        String[][] dateSpinnerItem = {{"今日", null},
                                      {"明日", null},
                                      {"来週の" + week[week_int -1] + "曜日", null},
                                      {"カレンダーから選ぶ", null}};
        dateAdapter = new ReminderSpinnerAdapter(getActivity());
        dateAdapter.setList(dateSpinnerItem, 0);
        dateAdapter.setTime(remindTime);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        remindTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
                        dateAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        remindTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
                        dateAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        remindTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 7);
                        dateAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        // 現在の時間を表すCalendarの年と月と日を0にリセット
                                        remindTime.set(year, month, dayOfMonth);
                                        dateAdapter.setTime(remindTime);
                                        dateAdapter.notifyDataSetChanged();
                                    }
                                }, year, month, dayOfMonth);
                        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
                        datePickerDialog.show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner timeSpinner = dialog.findViewById(R.id.spinner_time);
        // スピナー表示用のリスト　（例：朝, 8:05）
        Resources res = getResources();
        final int[][] prefTime = new int[4][2];
        for (int i = 0 ; i < prefKeyList.length ; i++) {
            String prefHour = prefKeyList[i] + "_hour_of_day";
            String prefMinute = prefKeyList[i] + "_minute";
            prefTime[i][0] = pref.getInt(prefHour, res.getInteger(res.getIdentifier(prefHour, "integer",  context.getPackageName())));
            prefTime[i][1] = pref.getInt(prefMinute, res.getInteger(res.getIdentifier(prefMinute, "integer",  context.getPackageName())));
        }

        // スピナー表示用のリスト　（例：朝, 8:05）
        final String[][] timeSpinnerItem = new String[5][2];
        String[] timeName = {"朝","昼","夕方","夜","時間を指定"};
        for(int i = 0 ; i < timeName.length ; i++) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                    timeSpinnerItem[i][0] = timeName[i];
                    timeSpinnerItem[i][1] = "" + prefTime[i][0] + ":" + String.format("%02d", prefTime[i][1]);
                    break;
                case 4:
                    timeSpinnerItem[i][0] = timeName[i];
                    break;
            }
        }

        timeAdapter = new ReminderSpinnerAdapter(getActivity());
        timeAdapter.setList(timeSpinnerItem, 1);
        timeSpinner.setAdapter(timeAdapter);
        timeAdapter.setTime(remindTime);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        remindTime.set(Calendar.HOUR_OF_DAY, prefTime[position][0]);
                        remindTime.set(Calendar.MINUTE, prefTime[position][1]);
                        timeAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // 現在の時間を表すCalendarの時間と分と秒を0にリセット
                                        setCalenderDate(remindTime, hourOfDay, minute);
                                        // 表示を更新するために、Adapterに指定した時間を渡す
                                        timeAdapter.setTime(remindTime);
                                        timeAdapter.notifyDataSetChanged();
                                    }
                                }, hour, minute, true);
                        timePickerDialog.show();
                        break;
                }
            }
              @Override
              public void onNothingSelected(AdapterView<?> parent) {}
          });

        Spinner repeatSpinner = dialog.findViewById(R.id.spinner_repeat);
        String[][] repeatSpinnerItem = {{"リピートなし", null},
                                        {"毎日", null},
                                        {"毎週", null},
                                        {"毎月", null},
                                        {"毎年", null},
                                        {"詳細設定", null}};
        repeatAdapter = new ReminderSpinnerAdapter(getActivity());
        repeatAdapter.setList(repeatSpinnerItem, 2);
        repeatSpinner.setAdapter(repeatAdapter);
        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    // 現在の時間を表すCalendarの時間と分と秒を0にリセット
                                    setCalenderDate(remindTime, hourOfDay, minute);
                                    todo.setNotifyStartTime(remindTime.getTimeInMillis());
                                    scheduleNotification("通知成功！", remindTime);
                                    // 表示を更新するために、Adapterに指定した時間を渡す
                                    repeatAdapter.setTime(remindTime);
                                }
                            }, hour, minute, true);
                    timePickerDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 削除ボタンの処理
        dialog.findViewById(R.id.button_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.where(Todo.class).equalTo("id", todoId).findFirst().deleteFromRealm();
                listener.onDeleteClicked();
                dialog.dismiss();
            }
        });

        // 保存ボタンの処理
        // ここで通知時間を確定する
        dialog.findViewById(R.id.button_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todoモデルの要素をセットし保存する
                todo.setId(todoId);
                todo.setNotifyStartTime(remindTime.getTimeInMillis());
                if (todo.createdAt == 0) {
                    todo.setCreatedAt(now.getTimeInMillis());
                } else {
                    todo.setUpdatedAt(now.getTimeInMillis());
                }
                saveRealmTodo(todo);
                scheduleNotification("通知成功！", remindTime);
                listener.onSaveClicked(todoId);
                dialog.dismiss();
            }
        });

        // キャンセルボタンの処理
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClicked();
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Activityが破棄される際にrealmのインスタンスを閉じる
        realm.close();
    }

    private void scheduleNotification(String content, Calendar calendar){

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("RequestCode",content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),requestCode, intent, 0);

        // アラームをセットする
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), pendingIntent);

            // トーストで設定されたことをを表示
            Toast.makeText(getActivity(),"リマインダーを設定しました", Toast.LENGTH_SHORT).show();
        }
    }

    private Calendar setCalenderDate(Calendar calendar, int hourOfDay, int minute){
        Calendar mCalendar = calendar;
        // 現在の時間を表すCalendarの時間と分と秒を0にリセット
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // タイムピッカーで指定した時間と分をセット
        calendar.add(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.add(Calendar.MINUTE, minute);
        return mCalendar;
    }

    public interface ReminderDialogFragmentListener {
        void onSaveClicked(long id);
        void onDeleteClicked();
        void onCancelClicked();
    }

    public void setReminderDialogFragmentListener(ReminderDialogFragmentListener listener) {
        this.listener = listener;
    }

    public void removeReminderDialogFragmentListener() {
        this.listener = null;
    }

    public void saveRealmTodo(final Todo todo){
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(todo);
                }
            });

            // recyclerViewの更新を求めるintentを送る
            Intent intent = new Intent("LIST_UPDATE");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } finally {
            Log.d("realm","saveTodo:success");
        }
    }
}