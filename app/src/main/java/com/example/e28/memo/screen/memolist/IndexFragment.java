package com.example.e28.memo.screen.memolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by User on 2019/11/24.
 */

public class IndexFragment extends FragmentActivity {

    Realm realm;

    TaggedRecyclerViewAdapter adapter;

    // DB変更の有無を受信してrecyclerViewを更新するレシーバー
    private BroadcastReceiver listUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Realmの初期化
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);


        setContentView(R.layout.fragment_index);

        //ノートタイトルを表示するRecyclerView
        realm = Realm.getDefaultInstance();
        RealmResults<Memo> memoRealmResults = realm.where(Memo.class).findAll();
        adapter = new TaggedRecyclerViewAdapter(memoRealmResults);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_index);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);

        // RecyclerView更新用のレシーバーを作成
        LocalBroadcastManager.getInstance(this).registerReceiver(listUpdateReceiver, new IntentFilter("LIST_UPDATE"));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        // realmのインスタンスを閉じる
        realm.close();
        // recyclerView更新用のレシーバーを破棄
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listUpdateReceiver);
    }
}
