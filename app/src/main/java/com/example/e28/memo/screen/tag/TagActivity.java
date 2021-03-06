package com.example.e28.memo.screen.tag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;
import com.example.e28.memo.screen.tagdialog.TagListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TagActivity extends AppCompatActivity {

    Realm realm;
    TagEditRecyclerViewAdapter adapter;

    Tag testtag1 = new Tag();
    Tag testtag2 = new Tag();
    Tag testtag3 = new Tag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        final EditText tagEditText = findViewById(R.id.edit_text_tag_name);
        Button savebtn = findViewById(R.id.button_save);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tag);

        // realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // とりあえずのタグ
        testtag1.setId(0);
        testtag1.setName("買い物");
        testtag2.setId(1);
        testtag2.setName("あとでやる");
        testtag3.setId(2);
        testtag3.setName("アイディア");
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(testtag1);
                    realm.copyToRealmOrUpdate(testtag2);
                    realm.copyToRealmOrUpdate(testtag3);
                }
            });
        } finally {
            Log.d("realm", "testTagSave:success");
        }


        // recyclerViewのクリックイベント
        RealmResults<Tag> memoRealmResults = realm.where(Tag.class).findAll();
        adapter = new TagEditRecyclerViewAdapter(memoRealmResults) {
            // onItemClick()をオーバーライドして
            // クリックイベントの処理を記述する

            @Override
            void onDeleteClick(long id) {
                    deleteRealmTag(id);
                    // recyclerViewの更新
            }

            @Override
            void onSaveClick(EditText tagEditText, int position, long id) {
                String tagName = tagEditText.getText().toString();
                if (!tagName.isEmpty()) {
                    Tag tag = new Tag();
                    tag.setUpdatedAt(new Date(System.currentTimeMillis()));
                    tag.setName(tagName);
                    saveRealmTag(tag, id);
                    // 記入欄をクリア
                    tagEditText.getEditableText().clear();
                } else {

                }
            }
        };

        // recyclerViewの設定
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        // 新規タグ作成
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = tagEditText.getText().toString();
                if (!tagName.isEmpty()) {
                    final Tag tag = new Tag();
                    tag.setCreatedAt(new Date(System.currentTimeMillis()));
                    tag.setName(tagName);
                    saveRealmTag(tag, getRealmTagNextId());
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // realmのインスタンスを閉じる
        realm.close();
    }

    void deleteRealmTag(final long id) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Tag.class).equalTo("id", id).findFirst().deleteFromRealm();
                }
            });
            adapter.notifyDataSetChanged();
        } finally {
            Log.d("realm", "delete:success");
        }
    }

    void saveRealmTag(final Tag tag, final long id) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    tag.setId(id);
                    realm.copyToRealmOrUpdate(tag);
                }
            });
            adapter.notifyDataSetChanged();
        } finally {
            Log.d("realm", "Save:success");
        }
    }

    public long getRealmTagNextId() {
        // 初期化
        long nextId = 0;

        Number maxId = realm.where(Tag.class).max("id");
        // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if (maxId != null) {
            nextId = maxId.longValue() + 1;
        }
        return nextId;
    }
}