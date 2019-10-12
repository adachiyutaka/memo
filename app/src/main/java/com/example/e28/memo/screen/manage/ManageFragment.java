package com.example.e28.memo.screen.manage;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/07/29.
 */

public class ManageFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preference_manage, rootKey);
    }

    // TODO: サマリーに現在の値を表示する
//    private void onCreateAppearancePreferences() {
//        // テーマ設定の現在の値をSummaryに表示
//        ListPreference themePreference = (ListPreference) findPreference("preference_theme");
//        themePreference.setSummary(themePreference.getEntry());
//        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                int indexOfValue = themePreference.findIndexOfValue(String.valueOf(newValue));
//                themePreference.setSummary(indexOfValue >= 0 ? themePreference.getEntries()[indexOfValue] : null);
//                return true;
//            }
//        });
//    }
}