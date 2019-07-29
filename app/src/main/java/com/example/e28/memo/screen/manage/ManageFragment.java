package com.example.e28.memo.screen.manage;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/07/29.
 */

public class ManageFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("settings");

        addPreferencesFromResource(R.layout.fragment_preference_manage);
    }
}