package com.example.e28.memo;

import android.app.Application;
import io.realm.Realm;

/**
 * Created by User on 2019/08/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}