package com.example.e28.memo.screen;

import java.lang.Number;
import java.util.ArrayList;
import java.util.Date;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by User on 2019/08/14.
 */

public class WriteActivity extends AppCompatActivity {

    Realm realm;
    Memo memo = new Memo();
    long memoId;
    ArrayList<Long> tagIdList = new ArrayList<>();
    EditText memoInput;

    public static final String TAG_LIST = "com.example.e28.memo.screen.TAG_LIST";
    static final int RESULT_TAG_LIST = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write);
        memoInput = findViewById(R.id.memoInput);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // Memoに新しいIdをセットする
        memoId = getRealmMemoNextId();
        memo.setId(memoId);
        saveRealmMemo(memo);

        // 保存ボタンでの保存と新規作成
        Button btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveMemo();
                // メモ入力エリアの表示をクリア
                memoInput.getEditableText().clear();

                // TODO: 2019/09/08 空白のデータ郡（タグなど）を追加する 
                // 新しいメモのidと空白のデータをセット
                memoId = getRealmMemoNextId();
            }
        });

        // タグボタン押下でダイアログを表示
        Button btnTag = findViewById(R.id.buttonTag);
        btnTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(WriteActivity.this, com.example.e28.memo.screen.tagdialog.TagDialogActivity.class);
                intent.putExtra(TAG_LIST, memoId);
                startActivityForResult(intent, RESULT_TAG_LIST);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Activityが隠れた際に入力された内容を自動保存する
        saveMemo();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Activityが破棄される際にrealmのインスタンスを閉じる
        realm.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_TAG_LIST && null != data) {
            tagIdList = (ArrayList<Long>) data.getSerializableExtra("TAG_LIST");
            Log.d("tagIdList", "" + tagIdList.size());
        }
        if (!tagIdList.isEmpty()) {
            memo.setIsTagged(true);
        }
    }

        // タグが変更された場合に、保存する
//        if(resultCode == RESULT_OK && requestCode == RESULT_TAG_LIST && null != data) {
//            tagIdList = (ArrayList<Long>)data.getSerializableExtra("TAG_LIST");
//            RealmList<Tag> tagRealmList = new RealmList<>();
//            for (Long id : tagIdList) {
//                tagRealmList.add(realm.where(Tag.class).equalTo("id", id).findFirst());
//            }
//            memo.setTagList(tagRealmList);
//        }

    public void saveMemo(){
        String memoInputStr = memoInput.getText().toString();
        // TODO: 2019/09/08 メモ内容が空白、タグなどは設定されている場合の処理を後で加える
        // memoInputに何も入力されていない場合、保存しない
        if (memoInputStr.isEmpty()) {
            return;
        } else {
            memo.setText(memoInputStr);
            memo.setCreatedAt(new Date(System.currentTimeMillis()));
            saveRealmMemo(memo);
        }
    }

    public void saveRealmMemo(final Memo memo){
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(memo);
                }
            });

            // recyclerViewの更新を求めるintentを送る
            Intent intent = new Intent("LIST_UPDATE");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } finally {
            Log.d("realm","saveMemo:success");
        }
    }

    public long getRealmMemoNextId() {
        // 初期化
        long nextId = 0;

        Number maxId = realm.where(Memo.class).max("id");
        // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if(maxId != null) {
            nextId = maxId.longValue() + 1;
        }
        return nextId;
    }
}