package com.example.e28.memo.screen.manage;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.e28.memo.R;
import com.example.e28.memo.screen.memolist.MainActivity;

import java.security.KeyManagementException;
import java.util.Calendar;

public class ManageActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

       //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // savedInstanceStateがnullでない場合は前回のFragmentが自動で復元されるのでnullの場合のみ処理
        if (savedInstanceState == null) {
            // トップ画面のFragmentを表示
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_fragment_manage, new ManageFragment())
                    .commit();
        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        switch (pref.getKey()) {
            case "pref_key_reminder_morning":
                Log.d("tag", "onPreferenceActivity: pref_key_reminder_morning");
                Toast.makeText(ManageActivity.this, "朝", Toast.LENGTH_LONG).show();

                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Toast.makeText(ManageActivity.this, hourOfDay+"時"+minute+"分", Toast.LENGTH_LONG).show();
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // ActionBarの矢印がクリックされたとき、Backボタンと同等の処理をする
            // TODO: 2019.10.13 そもそも矢印が表示されていない
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}