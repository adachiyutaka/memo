package com.example.e28.memo.screen.manage;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.TimePicker;
import com.example.e28.memo.R;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 2019/07/29.
 */

public class ManageFragment extends PreferenceFragmentCompat {

    Context context;
    SharedPreferences pref = null;
    Resources res;
    final String[] prefKeyList = {"pref_key_reminder_morning", "pref_key_reminder_afternoon", "pref_key_reminder_evening", "pref_key_reminder_night"};

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preference_manage, rootKey);

        context = this.getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        res = getResources();

        for (String key: prefKeyList){
            setTimePicker(key);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    // Preferenceが変更されたら、変更された値をサマリーにセット
    private SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String changedKey) {
                    for (String key: prefKeyList)
                    if (changedKey.equals(key))
                        // サマリーに変更された時間をセット
                        findPreference(key).setSummary(sharedPreferences.getString(key, res.getString(getResources().getIdentifier(key,"string", context.getPackageName()))));
                }
            };

    // TimePickerを設定し、入力された時間、分をPreferenceに保存する
    public void setTimePicker(final String prefName) {
        Preference timePref = findPreference(prefName);
        timePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // 設定されている時間（時間と分）を取得してタイムピッカーダイアログを表示する
                int prefDayOfHour = res.getInteger(res.getIdentifier(prefName + "_hour_of_day", "integer",  context.getPackageName()));
                int prefMinute = res.getInteger(res.getIdentifier(prefName + "_minute", "integer",  context.getPackageName()));
                final TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // TimePickerで設定した値をSharedPreferenceに書き込む
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(prefName, "" + hourOfDay + ":" + String.format("%02d", minute)); //　サマリーに表示する時間 "8:00" など
                                editor.putInt(prefName + "_hour_of_day", hourOfDay); // キー名の例：pref_key_reminder_morning_hour_of_day
                                editor.putInt(prefName + "_minute", minute); // キー名の例：pref_key_reminder_morning_minute
                                editor.commit();
                            }
                        }, prefDayOfHour, prefMinute, true);
                timePickerDialog.show();
                return false;
            }
        });
    }
}