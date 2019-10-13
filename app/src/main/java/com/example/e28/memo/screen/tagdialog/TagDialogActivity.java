package com.example.e28.memo.screen.tagdialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.e28.memo.R;
import com.example.e28.memo.model.Tag;
import com.example.e28.memo.screen.WriteActivity;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by User on 2019/08/11.
 */

public class TagDialogActivity extends AppCompatActivity {

    Realm realm;
    Intent intent;
    TagListRecyclerViewAdapter adapter;
    RealmResults<Tag> tagRealmResults;
    ArrayList<Long> tagIdList;
    ArrayList<Long> editedTagIdList = new ArrayList<>();
    Tag testtag1 = new Tag();
    Tag testtag2 = new Tag();
    Tag testtag3 = new Tag();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_tag_root);
        final EditText tagEditText = findViewById(R.id.edit_text_tag_name);
        Button savebtn = findViewById(R.id.button_save);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tag);
        final Button finishbtn = findViewById(R.id.button);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // 遷移先にタグidリストを送るためのintent生成
        intent = new Intent(TagDialogActivity.this, WriteActivity.class);

        // 遷移元からタグidリストを受け取る
        Intent receiveIntent = getIntent();
        tagIdList = (ArrayList<Long>) receiveIntent.getSerializableExtra(WriteActivity.TAG_ID_LIST);
        if (tagIdList != null) {
            editedTagIdList = tagIdList;
        } else {
            tagIdList = new ArrayList<>();
        }

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


        // すべてのタグのリストをリサイクラービューのアダプターにセット
        tagRealmResults = realm.where(Tag.class).findAll();
        adapter = new TagListRecyclerViewAdapter(tagRealmResults, editedTagIdList);

        adapter.setOnTagCheckListener(new TagListRecyclerViewAdapter.OnTagCheckListener() {
            @Override
            public void onChangeCheck(boolean isChecked, int position) {
                Log.d("RECYCLERVIEW", "onClick: positionは" + position + "isChecked : " + isChecked);
                long tagId = tagRealmResults.get(position).getId();
                if(isChecked){
                    editedTagIdList.add(tagId);
                }else {
                    editedTagIdList.remove(tagId);
                }
            }
        });

        // recyclerViewの設定、アダプターのセット
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        // 新規タグ作成の保存ボタン
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = tagEditText.getText().toString();
                if (!tagName.isEmpty()) {
                    // 新しいタグを生成し、Realmに保存
                    final Tag tag = new Tag();
                    long newTagId = getRealmTagNextId();
                    tag.setId(newTagId);
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
                        Log.d("realm", "TagSave:success");
                    }
                    // editedTagIdListに新しいタグを追加
                    editedTagIdList.add(newTagId);
                    putExtraTagIdList();
                    // タグ名の入力エリアをクリアー
                    tagEditText.getEditableText().clear();
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
                putExtraTagIdList();
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
            putExtraTagIdList();
        }else{
            // タグに変更がない場合は保存しない
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        adapter.deleteOnTagCheckListener();
        // realmのインスタンスを閉じる
        realm.close();
    }

    public void putExtraTagIdList() {
        intent.putExtra(WriteActivity.TAG_ID_LIST, editedTagIdList);
        setResult(RESULT_OK, intent);
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