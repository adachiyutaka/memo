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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by User on 2019/08/14.
 */

public class WriteActivity extends AppCompatActivity {

    Realm realm;
    Memo memo = new Memo();
    long memoId;
    ArrayList<Long> tagIdList = new ArrayList<>();
    EditText memoInput;
    ToggleButton highlightBtn;

    public static final String TAG_ID_LIST = "com.example.e28.memo.screen.TAG_LIST";
    public static final int RESULT_TAG_LIST = 0;

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
                highlightBtn.setChecked(false);

                // 新しいMemoモデルと新しいidをセット
                memoId = getRealmMemoNextId();
                memo = new Memo();
            }
        });

        // タグボタン押下でダイアログを表示
        Button btnTag = findViewById(R.id.buttonTag);
        btnTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(WriteActivity.this, com.example.e28.memo.screen.tagdialog.TagDialogActivity.class);
                // Memoが持つタグのidリストを生成し、中身が0でない場合はintentで渡す
                if (memo.getTagList() != null) {
                    for (Tag tag : memo.getTagList()) {
                        tagIdList.add(tag.getId());
                    }
                intent.putExtra(TAG_ID_LIST, tagIdList);}
                startActivityForResult(intent, RESULT_TAG_LIST);
            }
        });

        // ハイライトボタンでハイライトの設定
        highlightBtn = findViewById(R.id.toggleButton);
        highlightBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    memo.setIsHighlight(true);
                } else {
                    memo.setIsHighlight(false);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Activityが隠れた際に入力された内容を自動保存する
        saveMemo();
        // タグの内容をすべて消す
        tagIdList = new ArrayList<>();
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

        // タグのIDリストをExtraから得る
        if (requestCode == RESULT_TAG_LIST && null != data) {
            tagIdList = (ArrayList<Long>) data.getSerializableExtra(TAG_ID_LIST);
            // タグのIDリストからタグのリストを作成
            RealmList<Tag> tagRealmList = new RealmList<>();
            for (Long id : tagIdList) {
                tagRealmList.add(realm.where(Tag.class).equalTo("id", id).findFirst());
            }
            memo.setTagList(tagRealmList);
        }
        // 1つでもタグがセットされている場合、IsTaggedをセットする
        if (!tagIdList.isEmpty()) {
            memo.setIsTagged(true);
        }
        saveMemo();
    }


    public void saveMemo(){
        String memoInputStr = memoInput.getText().toString();
        // TODO: 2019/10/10 ハイライト、リマインダー、TODOが設定されている場合の処理を後で加える
        // memoInputに何も入力されおらず、い場合、保存しない
        if (memoInputStr.isEmpty() && !memo.getIsTagged()) {
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