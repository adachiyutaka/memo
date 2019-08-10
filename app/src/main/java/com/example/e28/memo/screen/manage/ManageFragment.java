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
}