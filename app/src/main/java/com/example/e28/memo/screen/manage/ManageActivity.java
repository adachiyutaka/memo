package com.example.e28.memo.screen.manage;

import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import com.example.e28.memo.R;

public class ManageActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        // savedInstanceStateがnullでない場合は前回のFragmentが自動で復元されるのでnullの場合のみ処理
        if (savedInstanceState == null) {
            // トップ画面のFragmentを表示
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_manage, new ManageFragment())
                    .commit();
        }
    }

    /*
    // PreferenceScreenがクリックされた時に呼び出されます
    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        // Fragmentの切り替えと、addToBackStackで戻るボタンを押した時に前のFragmentに戻るようにする
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment.newInstance(pref.getKey()))
                .addToBackStack(null)
                .commit();
        return true;
    }

    */
}