package com.example.e28.memo.screen;

import java.lang.Number;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Repeat;
import com.example.e28.memo.model.Tag;
import com.example.e28.memo.model.Todo;
import com.example.e28.memo.screen.reminder.ReminderDialogFragment;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by User on 2019/08/14.
 */

public class WriteActivity extends AppCompatActivity {

    // 通知確認テスト
    private AlarmManager am;
    private PendingIntent pending;
    private int requestCode = 1;
    //

    Realm realm;
    Memo memo = new Memo();
    long memoId;
    long todoId;
    ArrayList<Long> tagIdList = new ArrayList<>();
    EditText memoInput;
    ToggleButton highlightBtn;

    public static final String TAG_ID_LIST = "com.example.e28.memo.screen.TAG_LIST";
    public static final int RESULT_TAG_LIST = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write);
        memoInput = findViewById(R.id.edit_text_memo_Input);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // Memoに新しいIdをセットする
        memoId = getRealmNextId("Memo");
        todoId = getRealmNextId("Todo");
        memo.setId(memoId);
        saveRealmMemo(memo);

        // 保存ボタンでの保存と新規作成
        Button btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveMemo();
                // メモ入力エリアの表示をクリア
                memoInput.getEditableText().clear();
                highlightBtn.setChecked(false);

                // 新しいMemoモデルと新しいidをセット
                memoId = getRealmNextId("Memo");
                memo = new Memo();
            }
        });

        // タグボタン押下でダイアログを表示
        Button btnTag = findViewById(R.id.button_tag);
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

        // リマインダーボタン押下でダイアログを表示
        Button reminderBtn = findViewById(R.id.button_reminder);
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderDialogFragment reminderDialogFragment = new ReminderDialogFragment();
                reminderDialogFragment.setReminderDialogFragmentListener(new ReminderDialogFragment.ReminderDialogFragmentListener() {
                    @Override
                    public void onReturnValue(long id) {

                    }
                });
                reminderDialogFragment.show(getSupportFragmentManager(), "dialog");
            }
        });

        // ハイライトボタンでハイライトの設定
        highlightBtn = findViewById(R.id.toggle_button_highlight);
        highlightBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    memo.setHighlight(true);
                } else {
                    memo.setHighlight(false);
                }
            }
        });





        // 通知確認テスト
        Button notificationButton = findViewById(R.id.button_notification);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                // 10sec
                calendar.add(Calendar.SECOND, 10);

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("RequestCode",requestCode);

                pending = PendingIntent.getBroadcast(
                        getApplicationContext(),requestCode, intent, 0);

                // アラームをセットする
                am = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (am != null) {
                    am.setExact(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), pending);

                    // トーストで設定されたことをを表示
                    Toast.makeText(getApplicationContext(),
                            "alarm start", Toast.LENGTH_SHORT).show();

                    Log.d("debug", "start");
                }
            }
        });
    }
    //




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
            memo.setTagged(true);
        }
        saveMemo();
    }


    public void saveMemo(){
        String memoInputStr = memoInput.getText().toString();
        // TODO: 2019/10/10 ハイライト、リマインダー、TODOが設定されている場合の処理を後で加える
        // memoInputに何も入力されおらず、い場合、保存しない
        if (memoInputStr.isEmpty() && !memo.isTagged()) {
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

    public long getRealmNextId(String modelName) {
        // 初期化
        long nextId = 0;
        Number maxId;

        switch (modelName) {
            case "Memo":
                maxId = realm.where(Memo.class).max("id");
                // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
                if(maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                break;
            case "Todo":
                maxId = realm.where(Todo.class).max("id");
                // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
                if(maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                break;
            case "Repeat":
                maxId = realm.where(Repeat.class).max("id");
                // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
                if(maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                break;
        }
        return nextId;
    }
}