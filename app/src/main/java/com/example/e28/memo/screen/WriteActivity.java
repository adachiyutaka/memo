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
    Memo Memo;

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
        Memo.setId(getRealmMemoNextId());

        // 保存ボタンでの保存と新規作成
        Button btnSave = (Button) this.findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Memo.setText(memoInput.getText().toString());
                saveRealmMemo(Memo);
                // メモ入力エリアの表示をクリア
                memoInput.getEditableText().clear();
                // 新しいメモのidをセット
                Memo.setId(getRealmMemoNextId());
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

    public void saveRealmMemo(final Memo Memo){
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(Memo);
                }
            });
        } finally {
            Log.d("realm","saveMemo:success");
            realm.close();
        }
    }

    public void saveMemo(final Memo Memo){
        Memo.setText(memoInput.getText().toString());
        saveRealmMemo(Memo);
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
        // userIdの最大値を取得
        Number maxId = realm.where(Memo.class).max("id");
        // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if(maxId != null) {
            nextId = maxId.intValue() + 1;
        }
        return nextId;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 他のActivityに遷移したタイミングで保存
        saveMemo(Memo);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}