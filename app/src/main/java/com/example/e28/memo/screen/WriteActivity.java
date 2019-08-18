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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance(); //Realmのインスタンスを生成

        setContentView(R.layout.activity_write);
        num =(EditText)findViewById(R.id.memoNumber);
        memoInput=(EditText)findViewById(R.id.memoInput);
        numInput=(EditText)findViewById(R.id.numInput);

        Button btnSave = (Button) this.findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Update();
            }
        });

        Button btnLoad = (Button) this.findViewById(R.id.buttonLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load();
            }
        });
    }

    public void Update(){
        memoInputBuf = memoInput.getText().toString();
        numBuf = Long.parseLong(numInput.getText().toString());

//        numBuf = getRealmInvoiceModelNextId();

        // Realmの処理ここから
        //トランザクション
        realm.beginTransaction(); //トランザクション開始
        Memo memo =realm.createObject(Memo.class, numBuf); //realmのオブジェクトを作る
        memo.setText(memoInputBuf); //変数comment_bufを入れる
        realm.commitTransaction(); //トランザクションコミット
        // Realmの処理ここまで

        Toast.makeText(this,"更新しました",Toast.LENGTH_SHORT).show();
    }

    public void Load(){
        numBuf = Long.parseLong(num.getText().toString());
        
        // Realmの処理ここから
        Memo memo = realm.where(Memo.class)
                    .equalTo("id", numBuf)
                    .findFirst();

        memoViewBuf = memo.getText();

        Toast.makeText(this, memoViewBuf, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy(){ //閉じる処理
        super.onDestroy();
        realm.close();
    }
}