package com.example.e28.memo.screen;

import java.lang.Number;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.e28.memo.screen.reminder.FragmentClass;
import com.example.e28.memo.screen.reminder.ReminderDialogFragment;
import com.example.e28.memo.screen.reminder.RepeatDialogFragment;

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
    long todoId;
    ArrayList<Long> tagIdList = new ArrayList<>();
    RealmList<Todo> todoRealmList = new RealmList<>();

    EditText memoInput;
    ToggleButton highlightBtn;
    Button fragmentBtn;

    ReminderDialogFragment reminderDialogFragment;
    public static final String MEMO_ID = "com.example.e28.memo.screen.MEMO_ID";
    public static final String TAG_ID_LIST = "com.example.e28.memo.screen.TAG_LIST";
    public static final String TODO_ID = "com.example.e28.memo.screen.TODO_ID";
    public static final int RESULT_TAG_LIST = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write);
        memoInput = findViewById(R.id.edit_text_memo_Input);

        // Realmのインスタンスを生成
        realm = Realm.getDefaultInstance();

        // Memoに新しいIdをセットする
        memo = new Memo();
        memoId = getRealmNextId("Memo");
        memo.setId(memoId);

        // 保存ボタンでの保存と新規作成
        Button btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
                // メモ入力エリアの表示をクリア
                memoInput.getEditableText().clear();
                highlightBtn.setChecked(false);

                // 新しいMemoモデルと新しいidをセット
                memoId = getRealmNextId("Memo");
                memo = new Memo();
                memo.setId(memoId);
            }
        });

        // タグボタン押下でダイアログを表示
        Button btnTag = findViewById(R.id.button_tag);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteActivity.this, com.example.e28.memo.screen.tagdialog.TagDialogActivity.class);
                // Memoが持つタグのidリストを生成し、中身が0でない場合はintentで渡す
                if (memo.getTagList() != null) {
                    for (Tag tag : memo.getTagList()) {
                        tagIdList.add(tag.getId());
                    }
                    intent.putExtra(TAG_ID_LIST, tagIdList);
                }
                startActivityForResult(intent, RESULT_TAG_LIST);
            }
        });

        // リマインダーボタン押下でダイアログを表示
        Button reminderBtn = findViewById(R.id.button_reminder);
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderDialogFragment = new ReminderDialogFragment();

                // MemoのID、TodoのIDをReminderDialogに渡す
                Bundle bundle = new Bundle();
                bundle.putLong(MEMO_ID, memo.getId());
                if (memo.isTodo) {
                    // 既にTodoのIDが設定されている場合は、memoから読み取る
                    bundle.putLong(TODO_ID, todoRealmList.get(0).getId());
                } else {
                    // まだTodoのIDが設定されていない場合は新規作成する
                    bundle.putLong(TODO_ID, getRealmNextId("Todo"));
                }
                reminderDialogFragment.setArguments(bundle);
                reminderDialogFragment.show(getSupportFragmentManager(), "dialog");

                // リマインダーダイアログ上のボタンのクリック処理
                reminderDialogFragment.setReminderDialogFragmentListener(new ReminderDialogFragment.ReminderDialogFragmentListener() {
                    @Override
                    public void onSaveClicked(long todoId) {
                        // リマインダーの保存ボタン
                        memo.setTodo(true);
                        todoRealmList = new RealmList<>();
                        todoRealmList.add(realm.where(Todo.class).equalTo("id", todoId).findFirst());
                        memo.setTodoList(todoRealmList);
                    }

                    @Override
                    public void onDeleteClicked() {
                        // リマインダーの削除ボタン
                        memo.setTodo(false);
                    }

                    @Override
                    public void onCancelClicked() {
                        // リマインダーのキャンセルボタン
                    }
                });
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
    public void onDestroy() {
        super.onDestroy();
        // Activityが破棄される際にrealmのインスタンスを閉じる
        realm.close();
        // 登録したリスナーを解除する
        reminderDialogFragment.removeReminderDialogFragmentListener();
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


    public void saveMemo() {
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

    public void saveRealmMemo(final Memo memo) {
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
            Log.d("realm", "saveMemo:success");
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
                if (maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                break;
            case "Todo":
                maxId = realm.where(Todo.class).max("id");
                // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
                if (maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                break;
            case "Repeat":
                maxId = realm.where(Repeat.class).max("id");
                // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
                if (maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                break;
        }
        return nextId;
    }
}