package com.example.e28.memo.screen.manage;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/07/29.
 */

public class ManageFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preference_manage, rootKey);

        final EditTextPreference reminderTimeMorning = (EditTextPreference) findPreference("pref_key_reminder_morning");
        reminderTimeMorning.setSummary(reminderTimeMorning.getText());
        reminderTimeMorning.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                reminderTimeMorning.setSummary(newValue.toString());
                return true;
            }
        });
    }
}