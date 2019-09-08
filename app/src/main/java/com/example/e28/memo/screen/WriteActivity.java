package com.example.e28.memo.screen;

import java.lang.Number;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;

import io.realm.Realm;;
import io.realm.RealmResults;

/**
 * Created by User on 2019/08/14.
 */

public class WriteActivity extends AppCompatActivity {

    EditText numInput;
    EditText memoInput;
    EditText num;
    long numBuf;
    String memoInputBuf;
    String memoViewBuf;
    Realm realm;
    Memo memo = new Memo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        num =(EditText)findViewById(R.id.memoNumber);
        memoInput=(EditText)findViewById(R.id.memoInput);
        numInput=(EditText)findViewById(R.id.numInput);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // Memoに新しいIdをセットする
        memo.setId(getRealmMemoNextId());

        // 保存ボタンでの保存と新規作成
        Button btnSave = (Button) this.findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                memo.setText(memoInput.getText().toString());
                saveRealmMemo(memo);
                // メモ入力エリアの表示をクリア
                memoInput.getEditableText().clear();
                // 新しいメモのidをセット
                memo.setId(getRealmMemoNextId());
            }
        });
/*
        Button btnLoad = (Button) this.findViewById(R.id.buttonLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load();
            }
        });
 */
    }

    public void saveRealmMemo(final Memo memo){
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(memo);
                }
            });
        } finally {
            Log.d("realm","saveMemo:success");
            realm.close();
        }
    }

    public void saveMemo(final Memo memo){
        memo.setText(memoInput.getText().toString());
        saveRealmMemo(memo);
    }

/*
    public void Load(){
        numBuf = Long.parseLong(num.getText().toString());

        // Realmの処理ここから
        Memo memo = realm.where(Memo.class)
                    .equalTo("id", numBuf)
                    .findFirst();
        memoViewBuf = memo.getText();
        Toast.makeText(this, memoViewBuf, Toast.LENGTH_LONG).show();
    }
*/

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

        // TODO: 2019/09/08 メモ内容が空白、タグなどは設定されている場合の処理を後で加える
        // 他のActivityに遷移したタイミングで保存
        // メモの内容が空白の場合、保存しない
        if (!memo.getText().isEmpty()) {
            return;
        } else {
            saveMemo(memo);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}