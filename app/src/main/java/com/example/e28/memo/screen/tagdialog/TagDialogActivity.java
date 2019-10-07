package com.example.e28.memo.screen.tagdialog;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;
import com.example.e28.memo.screen.WriteActivity;
import com.example.e28.memo.screen.memolist.TaggedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.ToDoubleBiFunction;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by User on 2019/08/11.
 */

public class TagDialogActivity extends AppCompatActivity {

    Realm realm;
    Intent intent;
    Memo memo = new Memo();
    ArrayList<Long> tagIdList = new ArrayList();
    ArrayList<Long> editedTagIdList = new ArrayList();
    Tag testtag1 = new Tag();
    Tag testtag2 = new Tag();
    Tag testtag3 = new Tag();
    TagListRecyclerViewAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_tag_root);
//        final CheckBox chkbox1 = findViewById(R.id.tag1);
//        final CheckBox chkbox2 = findViewById(R.id.tag2);
//        final CheckBox chkbox3 = findViewById(R.id.tag3);
        final EditText tagEditText = findViewById(R.id.edit_text_tag_name);
        Button savebtn = findViewById(R.id.button_save);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tag);
        final Button finishbtn = findViewById(R.id.button);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // intentで渡されたMEMO_IDからUnmanagedの状態でMemoインスタンスを作成
        intent = getIntent();
        memo = realm.where(Memo.class).equalTo("id", intent.getLongExtra(WriteActivity.TAG_ID_LIST, -1)).findFirst();
//        memo = realm.copyFromRealm(realm.where(Memo.class).equalTo("id", getIntent().getLongExtra("MEMO_ID", -1)).findFirst());
//        if (memo.getTagList().size() != 0) {
//            for (Tag tag : memo.getTagList()) {
//                tagIdList.add(tag.getId());
//            }
//        }

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
        // とりあえずのタグ


        RealmResults<Tag> memoRealmResults = realm.where(Tag.class).findAll();
            adapter = new TagListRecyclerViewAdapter(memoRealmResults) {
                // onItemClick()をオーバーライドして
                // クリックイベントの処理を記述する
                @Override
                void onItemClick(CheckBox tagChk, int position, Tag tag) {
                    if (tagChk.isChecked() == true) {
                        editedTagIdList.add(tag.getId());
                    } else {
                        editedTagIdList.remove(tag.getId());
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
                    tag.setId(getRealmTagNextId());
                    tag.setCreatedAt(new Date(System.currentTimeMillis()));
                    tag.setName(tagName);

                    try {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(tag);
                            }
                        });
                    } finally {
                        Log.d("realm", "testTagSave:success");
                    }

                    // recyclerViewの更新
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // recyclerView更新用のレシーバーを作成
        // LocalBroadcastManager.getInstance(this).registerReceiver(listUpdateReceiver, new IntentFilter("LIST_UPDATE"));

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagDialogActivity.this, WriteActivity.class);
                intent.putExtra(WriteActivity.TAG_ID_LIST, editedTagIdList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        // タグが登録されている場合は
        if (!Objects.equals(editedTagIdList, tagIdList)) {
            // タグに変更がある場合は保存する
            intent.putExtra(WriteActivity.TAG_ID_LIST, editedTagIdList);
            setResult(RESULT_OK, intent);
        }else{
            // タグに変更がない場合は保存しない
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        // realmのインスタンスを閉じる
        realm.close();
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

    // 保存処理
//    public void saveMemo(final Memo memo) {
//        if (!Objects.equals(memo.gettagIdList(), tagIdList)) {
//            // タグに変更がある場合は保存する
//            memo.settagIdList(tagIdList);
//            saveRealmMemo(memo);
//        }else{
//            // タグに変更がない場合は保存しない
//        }
//}
//
//    public void saveRealmMemo(final Memo memo) {
//        try {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.copyToRealmOrUpdate(memo);
//                }
//            });
//            // recyclerViewの更新を求めるintentを送る
//            Intent intent = new Intent("LIST_UPDATE");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        } finally {
//            Log.d("realm", "saveMemo:success");
//        }
//    }
}