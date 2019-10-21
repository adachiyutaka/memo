package com.example.e28.memo.screen.reminder;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.example.e28.memo.model.Repeat;
import com.example.e28.memo.model.Todo;
import com.example.e28.memo.screen.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 2019/10/14.
 */

public class ReminderDialogFragment extends DialogFragment {

    Todo todo;
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

        todo = new Todo();

        Dialog dialog = new Dialog(getActivity());
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_reminder);

        // TODO: 2019/10/14 スピナーの表示はカスタムアダプターで設定する必要あり（選択項目と確定項目の表示が違う、選択項目に日付を入れるなど）

        final Calendar calendar = Calendar.getInstance();
        final Calendar now = calendar;
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String[] week = new String[7];
        week[0] = "日";
        week[1] = "月";
        week[2] = "火";
        week[3] = "水";
        week[4] = "木";
        week[5] = "金";
        week[6] = "土";

        int week_int = calendar.get(calendar.DAY_OF_WEEK);//曜日を数値で取得

        Spinner dateSpinner = dialog.findViewById(R.id.spinner_date);
        String[][] dateSpinnerItem = {{"今日", null},
                                      {"明日", null},
                                      {"来週の" + week[week_int -1] + "曜日", null},
                                      {"カレンダーから選ぶ", null}};
        dateAdapter = new ReminderSpinnerAdapter(getActivity());
        dateAdapter.setList(dateSpinnerItem, 0);
        dateAdapter.update(now);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        calendar.set(dayOfMonth, now.get(dayOfMonth) + 1);
                        break;
                    case 2:
                        calendar.set(dayOfMonth, now.get(dayOfMonth) + 7);
                        break;
                    case 3:
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        // 現在の時間を表すCalendarの年と月と日を0にリセット
                                        calendar.set(year, month, dayOfMonth);
                                        dateAdapter.update(calendar);
                                        dateAdapter.notifyDataSetChanged();
                                    }
                                }, year, month, dayOfMonth);
                        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
                        datePickerDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner timeSpinner = dialog.findViewById(R.id.spinner_time);
        String[][] timeSpinnerItem = {{"朝", pref.getInt("pref_key_reminder_morning_hour_of_day", -1) + ":" + pref.getInt("pref_key_reminder_morning_minute", -1)},
                                      {"昼", pref.getInt("pref_key_reminder_afternoon_hour_of_day", -1) + ":" + pref.getInt("pref_key_reminder_afternoon_minute", -1)},
                                      {"夕方", pref.getInt("pref_key_reminder_evening_hour_of_day", -1) + ":" + pref.getInt("pref_key_reminder_evening_minute", -1)},
                                      {"夜", pref.getInt("pref_key_reminder_night_hour_of_day", -1) + ":" + pref.getInt("pref_key_reminder_night_minute", -1)},
                                      {"時間を指定", null}};
        timeAdapter = new ReminderSpinnerAdapter(getActivity());
        timeAdapter.setList(timeSpinnerItem, 1);
        timeSpinner.setAdapter(timeAdapter);
        timeAdapter.update(now);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  if (position == 4) {
                      final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                              new TimePickerDialog.OnTimeSetListener() {
                                  @Override
                                  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                      // 現在の時間を表すCalendarの時間と分と秒を0にリセット
                                      setCalenderDate(calendar, hourOfDay, minute);
                                      // 表示を更新するために、Adapterに指定した時間を渡す
                                      timeAdapter.update(calendar);
                                      timeAdapter.notifyDataSetChanged();
                                  }
                              }, hour, minute, true);
                      timePickerDialog.show();
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
        repeatAdapter.setList(repeatSpinnerItem, 3);
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
                                    setCalenderDate(calendar, hourOfDay, minute);
                                    todo.setNotifyStartTime(calendar.getTime());
                                    scheduleNotification("通知成功！", calendar);
                                    // 表示を更新するために、Adapterに指定した時間を渡す
                                    repeatAdapter.update(calendar);
                                }
                            }, hour, minute, true);
                    timePickerDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        dialog.findViewById(R.id.button_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // OK ボタンのリスナ
        dialog.findViewById(R.id.button_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setNotifyStartTime(calendar.getTime());
                listener.onReturnValue(todo.getId());
                scheduleNotification("通知成功！", calendar);
            }
        });
        // Close ボタンのリスナ
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return dialog;
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
        public void onReturnValue(long id);
    }

    public void setReminderDialogFragmentListener(ReminderDialogFragmentListener listener) {
        this.listener = listener;
    }

    public void deleteReminderDialogFragmentListener() {
        this.listener = null;
    }
}