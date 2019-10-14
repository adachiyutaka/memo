package com.example.e28.memo.screen.reminder;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.e28.memo.R;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 2019/10/14.
 */

public class ReminderDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_reminder);

        // TODO: 2019/10/14 スピナーの表示はカスタムアダプターで設定する必要あり（選択項目と確定項目の表示が違う、選択項目に日付を入れるなど）

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

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
        ArrayList<String> date_array = new ArrayList<>();
        date_array.add("今日");
        date_array.add("明日");
        date_array.add("来週の" + week[week_int -1] + "曜日");
        date_array.add("カレンダーから選ぶ");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, date_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);
        dateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3){
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    SharedPreferences pref = getActivity().getSharedPreferences("pref_key_reminder_morning", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putInt("pref_key_reminder_morning_hour_of_day", hourOfDay);
                                    editor.putInt("pref_key_reminder_morning_minute", minute);
                                    editor.commit();
                                    // サマリーに設定した時間をセット
                                }
                            }, hour, minute, true);
                    timePickerDialog.show();
                }
            }
        });


        Spinner timeSpinner = dialog.findViewById(R.id.spinner_time);
        Spinner repeatSpinner = dialog.findViewById(R.id.spinner_repeat);
        dialog.findViewById(R.id.button_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // OK ボタンのリスナ
        dialog.findViewById(R.id.button_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
