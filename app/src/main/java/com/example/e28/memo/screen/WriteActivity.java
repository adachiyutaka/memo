package com.example.e28.memo.screen;

import java.lang.Number;
import java.util.Date;
import android.content.Context;
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

import io.realm.Realm;

/**
 * Created by User on 2019/08/14.
 */

public class WriteActivity extends AppCompatActivity {

    Realm realm;
    Memo memo = new Memo();
    EditText memoInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write);
        memoInput = findViewById(R.id.memoInput);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // Memoに新しいIdをセットする
        memo.setId(getRealmMemoNextId());

        // 保存ボタンでの保存と新規作成
        Button btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                memo.setText(memoInput.getText().toString());
                saveMemo(memo);
                // メモ入力エリアの表示をクリア
                memoInput.getEditableText().clear();

                // TODO: 2019/09/08 空白のデータ郡（タグなど）を追加する 
                // 新しいメモのidと空白のデータをセット
                memo.setId(getRealmMemoNextId());
            }
        });

        Button btnTag = findViewById(R.id.buttonTag);
        btnTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(WriteActivity.this, com.example.e28.memo.screen.tagdialog.TagDialogActivity.class);
                startActivity(intent);
            }
        });

    }

    public void saveMemo(final Memo memo){
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

    @Override
    protected void onPause() {
        super.onPause();
        // Activityが隠れた際に入力された内容を自動保存する
        saveMemo(memo);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Activityが破棄される際にrealmのインスタンスを閉じる
        realm.close();
    }
}