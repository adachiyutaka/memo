package com.example.e28.memo.screen.memolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by User on 2019/11/24.
 */

public class IndexFragment extends Fragment {

    Realm realm;

    IndexRecyclerViewAdapter adapter;

    // DB変更の有無を受信してrecyclerViewを更新するレシーバー
    private BroadcastReceiver listUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        //ノートタイトルを表示するRecyclerView
        realm = Realm.getDefaultInstance();

        //旧レイアウト
        //RealmResults<Memo> memoRealmResults = realm.where(Memo.class).findAll();
        //adapter = new TaggedRecyclerViewAdapter(memoRealmResults);

        RealmResults<Tag> tagRealmResults = realm.where(Tag.class).findAll();
        adapter = new IndexRecyclerViewAdapter(tagRealmResults);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_index);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);

        // RecyclerView更新用のレシーバーを作成
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listUpdateReceiver, new IntentFilter("LIST_UPDATE"));

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        // realmのインスタンスを閉じる
        realm.close();
        // recyclerView更新用のレシーバーを破棄
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(listUpdateReceiver);
    }
}
