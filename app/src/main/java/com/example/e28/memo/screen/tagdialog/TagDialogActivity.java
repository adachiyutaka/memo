package com.example.e28.memo.screen.tagdialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;
import com.example.e28.memo.screen.WriteActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by User on 2019/08/11.
 */

public class TagDialogActivity extends AppCompatActivity {

    Realm realm;
    Intent intent;
    Memo memo = new Memo();
    ArrayList<Long> tagIdList = new ArrayList();
    ArrayList<Long> editedTagIdList = new ArrayList();
    Tag testtag = new Tag();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_tag_root);
        final CheckBox chkbox = findViewById(R.id.tag1);
        final Button finishbtn = findViewById(R.id.button);


        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // intentで渡されたMEMO_IDからUnmanagedの状態でMemoインスタンスを作成
        intent = getIntent();
        memo = realm.where(Memo.class).equalTo("id", intent.getLongExtra(WriteActivity.TAG_LIST, -1)).findFirst();
//        memo = realm.copyFromRealm(realm.where(Memo.class).equalTo("id", getIntent().getLongExtra("MEMO_ID", -1)).findFirst());
//        if (memo.getTagList().size() != 0) {
//            for (Tag tag : memo.getTagList()) {
//                tagIdList.add(tag.getId());
//            }
//        }

        // とりあえずのタグ
        testtag.setId(0);
        testtag.setName("買い物");

        // 各タグのチェック状態読み取り
        chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkbox.isChecked() == true) {
                    editedTagIdList.add(testtag.getId());
                } else {
                    editedTagIdList.remove(testtag.getId());
                }
            }
        });

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagDialogActivity.this, WriteActivity.class);
                intent.putExtra(WriteActivity.TAG_LIST, editedTagIdList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        // タグが登録されている場合は
//        if (!Objects.equals(editedTagIdList, tagIdList)) {
//            // タグに変更がある場合は保存する
//            intent.putExtra(WriteActivity.TAG_LIST, editedTagIdList);
//            setResult(RESULT_OK, intent);
//        }else{
//            // タグに変更がない場合は保存しない
//        }
//    }

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