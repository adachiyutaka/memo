package com.example.e28.memo.screen.memo_hole;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;
import com.example.e28.memo.screen.index.RecyclerViewAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

public class MemoHoleActivity extends AppCompatActivity {

    Realm realm;

    RecyclerViewAdapter adapter;

    RecyclerView recyclerView;

    ArrayList<String> datasource = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_);

        // realmのインスタンスを生成
        realm = Realm.getDefaultInstance();


        // テスト用のタグとメモを作成
        for (int i = 0; i < 10; i++ ) {
            final int I = i;

            // タグを作成
            try {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Tag tag = new Tag();
                        tag.setId(I);
                        tag.setName("タグその" + I);
                        realm.copyToRealmOrUpdate(tag);
                    }
                });
            } finally {
                Log.d("realm", "memoSave:success");
            }

            // メモを作成
            try {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // メモに持たせるためのタグのリストを作成
                        RealmList<Tag> tagList = new RealmList<>();
                        tagList.add(realm.where(Tag.class).equalTo("id", I).findFirst());

                        // メモを作成し、タグを設定
                        Memo memo = new Memo();
                        memo.setId(I);
                        memo.setText("メモその" + I);
                        memo.setTagged(true);
                        memo.setTagList(tagList);
                    }
                });
            } finally {
                Log.d("realm", "memoSave:success");
            }
        }

        recyclerView = findViewById(R.id.recycler_view_index_);
        adapter = new RecyclerViewAdapter(datasource);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }
}
