package com.example.e28.memo.screen.manage;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.TimePicker;
import com.example.e28.memo.R;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 2019/07/29.
 */

public class ManageFragment extends PreferenceFragmentCompat {

    final Calendar calendar = Calendar.getInstance();
    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    final int minute = calendar.get(Calendar.MINUTE);

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preference_manage, rootKey);

        final Context context = this.getActivity();

        final Preference reminderTimeMorning = findPreference("pref_key_reminder_morning");
        reminderTimeMorning.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                reminderTimeMorning.setSummary(newValue.toString());
                return true;
            }
        });

        reminderTimeMorning.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                SharedPreferences pref = getActivity().getSharedPreferences("pref_key_reminder_morning", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("pref_key_reminder_morning_hour_of_day", hourOfDay);
                                editor.putInt("pref_key_reminder_morning_minute", minute);
                                editor.commit();
                                // サマリーに設定した時間をセット
                                reminderTimeMorning.setSummary(hourOfDay + ":" + String.format("%02d", minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
                return false;
            }
        });
    }
}